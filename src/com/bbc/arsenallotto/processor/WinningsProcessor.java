/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.processor;

import com.bbc.arsenallotto.model.BaLottoNotifications;
import com.bbc.arsenallotto.model.BaLottoWinners;
import com.bbc.arsenallotto.model.BaLottoWinningNumbers;
import com.bbc.arsenallotto.model.BaLottoWinningsSummary;
import com.bbc.arsenallotto.model.BaLottoWinningsTotal;
import com.bbc.arsenallotto.model.controller.BaLottoEntryJpaController;
import com.bbc.arsenallotto.model.controller.BaLottoNotificationsJpaController;
import com.bbc.arsenallotto.model.controller.BaLottoWinnersJpaController;
import com.bbc.arsenallotto.model.controller.BaLottoWinningNumbersJpaController;
import com.bbc.arsenallotto.model.controller.BaLottoWinningsSummaryJpaController;
import com.bbc.arsenallotto.model.controller.BaLottoWinningsTotalJpaController;
import com.bbc.arsenallotto.service.BBCALLogger;
import com.bbc.arsenallotto.utilities.UtilityMethods;
import com.bbc.arsenallotto.winjsonresponse.FPrize;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Soul
 */
public class WinningsProcessor extends Thread {

    private boolean shutdown = false;

    @Override
    public void run() {
        HashMap<String, String> configs = UtilityMethods.getConfigs();
        int sleepInterval = Integer.parseInt(configs.get("winnings_sleep_interval")) * 1000;

        while (!shutdown) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("BBCArsenalLottoServicePU");
            BaLottoNotificationsJpaController notificationController = new BaLottoNotificationsJpaController(emf);
            List<BaLottoNotifications> notifications = notificationController.findQueuedNotifications();

            for (BaLottoNotifications notification : notifications) {
                Short gameId = notification.getGameId();
                String drawDate = notification.getDrawDate();

                String request = configs.get("winnings_url") + "/" + gameId + "/" + drawDate;
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
                        connection.setRequestMethod("GET");
                    } catch (ProtocolException ex) {
                        BBCALLogger.getLogger().log(Level.SEVERE, null, ex);
                    }
                    //connection.setRequestProperty("Content-Type", "application/xml");
                    connection.setRequestProperty("Accepts", "application/xml");
                    //connection.setRequestProperty("Preshared-Key", "ea34879b031f40b34e425b08975394ac");
                    //connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                    connection.setUseCaches(false);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()));
                    String decoded = "";
                    String decodedString = "";
                    while ((decodedString = in.readLine()) != null) {
                        //System.out.println(decodedString);
                        decoded += decodedString;
                    }

                    connection.disconnect();

                    //writeToTempFile(decoded);
                    BBCALLogger.getLogger().log(Level.INFO, decoded);
                    com.bbc.arsenallotto.winjsonresponse.Data response = null;
                    try {
                        JAXBContext context = JAXBContext.newInstance(com.bbc.arsenallotto.winjsonresponse.Data.class);
                        Unmarshaller unmarshaller = context.createUnmarshaller();
                        StringReader reader = new StringReader(decoded);
                        response = (com.bbc.arsenallotto.winjsonresponse.Data) unmarshaller.unmarshal(reader);

                        BaLottoWinningNumbersJpaController winningNosController = new BaLottoWinningNumbersJpaController(emf);
                        BaLottoWinningNumbers winningNos = winningNosController.findByNotificationId(notification.getId());
                        if (winningNos != null) {
                            notification.setBaLottoWinningNumbers(null);
                            BBCALLogger.getLogger().log(Level.INFO, "Winning Nos Exist, Deleting ... " + winningNos.getId());
                            winningNosController.destroy(winningNos.getId());
                        }
                        winningNos = new BaLottoWinningNumbers();

                        List<Integer> winningNosList = response.getoDraw().getaNumbers().getNumber();
                        winningNos.setEntry1(winningNosList.get(0));
                        winningNos.setEntry2(winningNosList.get(1));
                        winningNos.setEntry3(winningNosList.get(2));
                        winningNos.setEntry4(winningNosList.get(3));
                        winningNos.setEntry5(winningNosList.get(4));
                        if (winningNosList.size() > 5) {
                            winningNos.setEntry6(winningNosList.get(5));
                        }

                        Integer bonus = response.getoDraw().getaNumbers().getBonus();
                        if (bonus != null) {
                            winningNos.setLegend(bonus);
                        }
                        winningNos.setNotificationId(notification);
                        winningNosController.create(winningNos);
                        notification.setBaLottoWinningNumbers(winningNos);

                        BaLottoWinningsTotalJpaController winningsTotalController = new BaLottoWinningsTotalJpaController(emf);
                        BaLottoWinningsTotal winningsTotal = winningsTotalController.findByNotificationId(notification.getId());
                        if (winningsTotal != null) {
                            notification.setBaLottoWinningsTotal(null);
                            BBCALLogger.getLogger().log(Level.INFO, "Winning Total Exists, Deleting ... " + winningsTotal.getId());
                            winningsTotalController.destroy(winningsTotal.getId());
                        }
                        winningsTotal = new BaLottoWinningsTotal();

                        winningsTotal.setNotificationId(notification);
                        winningsTotal.setTotalPrize(BigDecimal.valueOf(response.getoDraw().getaPrizes().getfPrizePool()));
                        winningsTotalController.create(winningsTotal);
                        notification.setBaLottoWinningsTotal(winningsTotal);

                        BaLottoWinningsSummaryJpaController winningsSummaryController = new BaLottoWinningsSummaryJpaController(emf);
                        notification.setBaLottoWinningsSummaryCollection(null);
                        //BBCALLogger.getLogger().log(Level.INFO, "Winnings Summary Exists, Deleting ... ");
                        winningsSummaryController.destroyByNotificationId(notification.getId());

                        List<BaLottoWinningsSummary> summaryList = new ArrayList<>();
                        List<FPrize> fPrizeList = response.getoDraw().getaPrizes().getfPrize();
                        for (FPrize fPrize : fPrizeList) {
                            BaLottoWinningsSummary winningsSummary = new BaLottoWinningsSummary();

                            winningsSummary.setNotificationId(notification);
                            winningsSummary.setPrize(fPrize.getfPrize());
                            winningsSummary.setQuantity(fPrize.getiWinners());
                            winningsSummary.setTierId(fPrize.getiTier());
                            winningsSummary.setPrizeCode(fPrize.getiPrizeCode());
                            winningsSummary.setTierName(fPrize.getsTierName());

                            winningsSummaryController.create(winningsSummary);
                            summaryList.add(winningsSummary);
                        }
                        notification.setBaLottoWinningsSummaryCollection(summaryList);

                        BaLottoWinnersJpaController winnersController = new BaLottoWinnersJpaController(emf);
                        notification.setBaLottoWinnersCollection(null);
                        winnersController.destroyByNotificationId(notification.getId());

                        String winners = response.getoDraw().getWinners();
                        //winners = winners.replaceAll("\\\\n", "\n");
                        String[] winnersLoop = winners.split(";");

                        //List<BaLottoWinners> winList = new ArrayList<>();
                        //System.out.println("Winners :: " + StringEscapeUtils.unescapeXml(winners) + ":::" + winnersLoop.length);
                        for (int i = 0; i < winnersLoop.length; i++) {
                            String winnerString = winnersLoop[i];
                            //int j = i;
                            BBCALLogger.getLogger().log(Level.INFO, "OK. Processing this in a Thread :: " + winnerString);

                            // new Thread() {
                            //   public void run() {
                            String[] winnersList = winnerString.split(",");
                            BaLottoWinners winner = new BaLottoWinners();

                            winner.setLottoReference(new BaLottoEntryJpaController(emf).findByLottoReference(winnersList[0]));
                            winner.setNotificationId(notification);
                            winner.setOrderId(winnersList[1]);
                            winner.setTierId(Short.parseShort(winnersList[2]));

                            winnersController.create(winner);
                            //winList.add(winner);
                            BBCALLogger.getLogger().log(Level.INFO, "OK. Just Inserted Winner with index :: " + i);

                            /*if (i % 1000 == 0) {
                             notification.setBaLottoWinnersCollection(winList);
                             notificationController.edit(notification);
                             winList = new ArrayList<>();
                             }*/
                            /*if (j == winnersLoop.length) {
                             BBCALLogger.getLogger().log(Level.INFO, "About to Update :: " + j);
                             notificationController.markProcessed(notification.getId());
                             }*/
                            //         }
                            // }.start();
                        }

                        BBCALLogger.getLogger().log(Level.INFO, "About to Update Notification :: " + notification.getId());
                        notificationController.markProcessed(notification.getId());

                                   //while (true) {
                        //BBCALLogger.getLogger().log(Level.INFO, "OK. Comparing. Total Records :: " + winnersLoop.length + "..." + "Processed ::" + winList.size());
                        //if (winList.size() == winnersLoop.length) {
                        /*notification.setBaLottoWinnersCollection(winList);
                         notification.setProcessed(true);
                         notificationController.edit(notification);*/
                        //}
                        //Thread.sleep(5000);
                        //}
                    } catch (Exception e) {
                        response = null;
                        BBCALLogger.getLogger().log(Level.INFO, "Severe Exception Occured!", e);
                        BBCALLogger.getLogger().log(Level.SEVERE, null, e);
                    }
                    in.close();

                } catch (IOException ex) {
                    BBCALLogger.getLogger().log(Level.INFO, "Severe IO Exception Occured!", ex);
                    BBCALLogger.getLogger().log(Level.SEVERE, null, ex);
                }
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
