/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.service;

/**
 *
 * @author Soul
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;

public class Listener
  extends Thread
{
  private int a;
  
  public Listener(int port)
  {
    this.a = port;
  }
  
  public void run()
  {
    try
    {
      ServerSocket localServerSocket;
      (localServerSocket = new ServerSocket(this.a)).accept(); return;
    }
    catch (IOException localIOException)
    {
      BBCALLogger.getLogger().log(Level.SEVERE, "Exiting Now ...", localIOException);
      System.exit(0);
    }
  }
}
