/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bmstu.nets.server;

import com.bmstu.nets.server.logger.Logger;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;

/**
 *
 * @author patutinaam
 */
public class Server {

    private final int port;
    private static final Logger LOG = new Logger();
    private ServerSocket smtpSocket;

    public Server(int port)
    {
            this.port = port;
    }
    
    public void startSMTP() throws BindException {
        try {
            LOG.info("Starting SMTP  (▀̿̿Ĺ̯̿▀̿ ̿)");
            InetSocketAddress bindAddress = new InetSocketAddress(port);
            smtpSocket = new ServerSocket();
            smtpSocket.setReuseAddress(true);
            smtpSocket.bind(bindAddress);
            LOG.info("Socket Opened (ーー;)");
            while(true) {
                LOG.info("Connect me ('・ω・')");
                Socket connectionSocket = smtpSocket.accept();

            }
        } catch (IOException ex) {
            LOG.error("Failed to open socket (｡ŏ﹏ŏ)");
            ex.printStackTrace(System.err);        
        }
    }
    
   public static void main(String args[]) throws Exception
   {
           int bindPort = 2525;
           if(args.length == 1)
           {
                   LOG.info("Setting port to " + args[0]);
                   bindPort = Integer.parseInt(args[0]);
           }
           else
           {
                   LOG.info("Default to 2525 (︶^︶)");
           }
           Server console = new Server(bindPort);
           console.startSMTP();
   }
	
    
}
