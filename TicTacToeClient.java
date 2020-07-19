/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tic.tac.toe.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;

/**
 *
 * @author Brian
 */
public class TicTacToeClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         Client application; // declare client application

        // Check for command line args
        if (args.length == 0) {
            application = new Client("127.0.0.1"); // localhost
        } else {
            application = new Client(args[0]); // use args
        }

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
//                    Socket serverSocket = null;
//        try {
//            System.out.println("Client has started.");
//            serverSocket = new Socket("localhost", 6013);
//             BufferedReader serverInput = new BufferedReader
//                        (new InputStreamReader(serverSocket.getInputStream()));
//
//                // Establish the server's output stream.
//                DataOutputStream clientOutput = new DataOutputStream
//                                (serverSocket.getOutputStream());
//
//
//                // Create a server-game based version of TicTacToe.
//            
//                
//               PrintWriter pout = new PrintWriter(serverSocket.getOutputStream(), true);
//               //PrintWriter pout2 = new PrintWriter(client2.getOutputStream(), true);
//                // write the Date to the socket
//                InputStream scan = serverSocket.getInputStream();
//               DataInputStream input = new DataInputStream(scan);
//            System.out.println("You have connected to the server.");
//            RecieveData(pout, input);
//            serverSocket.close();
//        
//        } catch (Exception e) {
//            System.err.println("Connection has been failed and you have not connected to server." + e);
//            
//        } 
//            
       
    }
//    public static void RecieveData(PrintWriter print, DataInputStream data){
//               //client reads from the server
//        System.out.println("Recieved data");
//            try{
//            //data.readUTF();
//            //String read = data.readUTF();
//               // System.out.println(data.read());
//                byte bufferSize[]  = new byte[32];
//                data.read(bufferSize);
//                String str = new String(bufferSize);
//                System.out.println(str);
//                
//            }catch(IOException ie){
//                System.err.println(ie);
//            }
//                        // client send the data to the Server
//            print.println(new java.util.Date().toString());
//            
//    
//    }


