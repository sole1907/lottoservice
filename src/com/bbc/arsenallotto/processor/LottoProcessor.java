/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.processor;

import com.bbc.arsenallotto.jsonrequest.AItems;
import com.bbc.arsenallotto.jsonrequest.AOrderItems;
import com.bbc.arsenallotto.jsonrequest.Boards;
import com.bbc.arsenallotto.jsonrequest.Data;
import com.bbc.arsenallotto.jsonrequest.OCustomer;
import com.bbc.arsenallotto.jsonrequest.OGameData;
import com.bbc.arsenallotto.jsonrequest.OOrder;
import com.bbc.arsenallotto.jsonrequest.OTransaction;
import com.bbc.arsenallotto.jsonrequest.OrderItem;
import com.bbc.arsenallotto.model.BaLottoEntry;
import com.bbc.arsenallotto.model.BaLottoEntryDetails;
import com.bbc.arsenallotto.model.controller.BaLottoEntryJpaController;
import com.bbc.arsenallotto.service.BBCALLogger;
import com.bbc.arsenallotto.utilities.UtilityMethods;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Soul
 */
public class LottoProcessor extends Thread {

    private boolean shutdown = false;
    
    @Override
    public void run() {
        HashMap<String, String> configs = UtilityMethods.getConfigs();
        int sleepInterval = Integer.parseInt(configs.get("sleep_interval")) * 1000;
        int maxAttempts = Integer.parseInt(configs.get("max_attempts"));
        int maxEntriesPerInterval = Integer.parseInt(configs.get("max_entries_per_interval"));

        while (!shutdown) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("BBCArsenalLottoServicePU");
            BaLottoEntryJpaController entryController = new BaLottoEntryJpaController(emf);
            List<BaLottoEntry> lottoEntries = entryController.findQueuedEntries("Updated", maxAttempts, maxEntriesPerInterval);

            for (BaLottoEntry lottoEntry : lottoEntries) {
                OrderItem orderItem = new OrderItem();
                orderItem.setiGameID(lottoEntry.getGameId());
                OGameData gameData = new OGameData();
                gameData.setPlayDuration(lottoEntry.getDuration());
                List<Integer> days = new ArrayList<>();
                if (lottoEntry.getDays() == 0) {
                    days.add(3);
                    days.add(6);
                } else {
                    days.add(new Integer(lottoEntry.getDays()));
                }
                gameData.setDay(days);

                List<Boards> boards = new ArrayList<>();
                for (BaLottoEntryDetails entryDetail : lottoEntry.getBaLottoEntryDetailsCollection()) {
                    Boards board = new Boards();
                    List<Integer> numbers = new ArrayList<>();
                    numbers.add(entryDetail.getEntry1());
                    numbers.add(entryDetail.getEntry2());
                    numbers.add(entryDetail.getEntry3());
                    numbers.add(entryDetail.getEntry4());
                    numbers.add(entryDetail.getEntry5());
                    if (entryDetail.getEntry6() != null) {
                        numbers.add(entryDetail.getEntry6());
                    }
                    if (entryDetail.getLegend() != null) {
                        numbers.add(entryDetail.getLegend());
                    }
                    board.setNumbers(numbers);
                    boards.add(board);
                }
                gameData.setBoards(boards);

                orderItem.setoGameData(gameData);

                AOrderItems aOrderItems = new AOrderItems();
                aOrderItems.setOrderItem(orderItem);

                OOrder order = new OOrder();
                SimpleDateFormat sdfd = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat sdft = new SimpleDateFormat("HHmmss");
                order.setdCreatedAt(sdfd.format(lottoEntry.getEntryDate()) + "T" + sdft.format(lottoEntry.getEntryDate()));
                order.setaOrderItems(aOrderItems);
                order.setfBalance(0f);
                order.setfTotal(lottoEntry.getAmount().floatValue());
                order.setiCurrency("46");

                AItems aItems = new AItems();
                aItems.setoOrder(order);

                OCustomer oCustomer = new OCustomer();
                oCustomer.setsEmail(lottoEntry.getMemberId().getEmail());
                oCustomer.setsFirstName(lottoEntry.getMemberId().getFirstname());
                oCustomer.setsLastName(lottoEntry.getMemberId().getLastname());
                oCustomer.setsPhone(lottoEntry.getMemberId().getMobileNumber());

                aItems.setoCustomer(oCustomer);

                OTransaction oTransaction = new OTransaction();
                oTransaction.setbApproved("true");
                oTransaction.setfAmount(lottoEntry.getAmount().floatValue());
                oTransaction.setsDetails("<![CDATA[... Response details from payment]]>");
                oTransaction.setsRef(lottoEntry.getLottoReference());

                aItems.setoTransaction(oTransaction);

                Data data = new Data();
                data.setaItems(aItems);
                StringWriter writter = null;

                try {
                    JAXBContext context = JAXBContext.newInstance(Data.class);
                    Marshaller marshaller = context.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    //marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

                    ObjectMapper mapper = new ObjectMapper();
                    writter = new StringWriter();
                    marshaller.marshal(data, writter);
                } catch (Exception e) {
                    BBCALLogger.getLogger().log(Level.SEVERE, null, e);
                }

                BBCALLogger.getLogger().log(Level.INFO, "Sending XML");
                BBCALLogger.getLogger().log(Level.INFO, writter.toString());
                BBCALLogger.getLogger().log(Level.INFO, "===================================================");

                //String urlParameters = "param1=a&param2=b&param3=c";
                String request = configs.get("lotto_url");
                URL url = null;
                try {
                    url = new URL(request);
                } catch (MalformedURLException ex) {
                    BBCALLogger.getLogger().log(Level.SEVERE, null, ex);
                }
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    try {
                        connection.setRequestMethod("POST");
                    } catch (ProtocolException ex) {
                        BBCALLogger.getLogger().log(Level.SEVERE, null, ex);
                    }
                    connection.setRequestProperty("Content-Type", "application/xml");
                    connection.setRequestProperty("Accepts", "application/xml");
                    connection.setRequestProperty("Preshared-Key", "ea34879b031f40b34e425b08975394ac");
                    //connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                    connection.setUseCaches(false);

                    DataOutputStream wr = null;
                    wr = new DataOutputStream(connection.getOutputStream());
                    wr.writeBytes(writter.toString());
                    wr.flush();
                    wr.close();

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()));
                    String decoded = "";
                    String decodedString = "";
                    while ((decodedString = in.readLine()) != null) {
                        //System.out.println(decodedString);
                        decoded += decodedString;
                    }

                    BBCALLogger.getLogger().log(Level.INFO, decoded);
                    com.bbc.arsenallotto.jsonresponse.Data response = null;
                    try {
                        JAXBContext context = JAXBContext.newInstance(com.bbc.arsenallotto.jsonresponse.Data.class);
                        Unmarshaller unmarshaller = context.createUnmarshaller();
                        StringReader reader = new StringReader(decoded);
                        response = (com.bbc.arsenallotto.jsonresponse.Data) unmarshaller.unmarshal(reader);

                        lottoEntry.setAction(response.getaOrderResponse().getItem().getsCustomerAction());
                        lottoEntry.setAttempts((short)(lottoEntry.getAttempts() + 1));
                        lottoEntry.setBookingRef(response.getaOrderResponse().getItem().getaBookingRefs().getItem());
                        lottoEntry.setOrderId(response.getaOrderResponse().getItem().getiOrderId());

                        entryController.edit(lottoEntry);
                        //em.getTransaction().commit();
                    } catch (Exception e) {
                        response = null;
                        BBCALLogger.getLogger().log(Level.SEVERE, null, e);
                    }
                    in.close();

                } catch (IOException ex) {
                    BBCALLogger.getLogger().log(Level.SEVERE, null, ex);
                }
                connection.disconnect();
            }

            try {
                Thread.sleep(sleepInterval);
            } catch (InterruptedException ex) {
                BBCALLogger.getLogger().log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void shutDown() {
        shutdown = true;
    }

}
