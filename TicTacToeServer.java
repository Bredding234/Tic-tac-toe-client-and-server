/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tic_Tac_Toe_Server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 *
 * @author Brian
 */
public class TicTacToeServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // Initializes the awaiting connection of server, it waits for the client to connect to each other.
        TicTacToeBoard application = new TicTacToeBoard();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.execute();
    
            //initialize the board
      //char[][] board = new char[13][13];
//        try{            
//          System.out.println("Lets start our connection.");
//         ServerSocket server = new ServerSocket(6013);        
//       // ServerSocket server2 = new ServerSocket(6015);
//       
//            // When welcomeSocket is contacted, it returns a socket to handle communication
//                // with the client.
//         
//             
//                Socket client = server.accept();
//                System.out.println("Connection is established for client1.");
//
//                //Socket client2 = server2.accept();
//               // System.out.println("Connection is established for client2.");
//
//                // we have a connection
//                // Establish the client's input stream.
//                
//                BufferedReader clientInput = new BufferedReader
//                        (new InputStreamReader(client.getInputStream()));
//
//                // Establish the server's output stream.
//                DataOutputStream serverOutput = new DataOutputStream
//                                (client.getOutputStream());
//                
//                  // Establish the client's input stream.
//                
//               // BufferedReader clientInput2 = new BufferedReader (new InputStreamReader(client2.getInputStream()));
//
//                // Establish the server's output stream.
//               // DataOutputStream serverOutput2 = new DataOutputStream (client2.getOutputStream());
//
//
//                // Create a server-game based version of TicTacToe.
//            
//                
//               PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
//               //PrintWriter pout2 = new PrintWriter(client2.getOutputStream(), true);
//                // write the Date to the socket
//                InputStream scan = client.getInputStream();
//               DataInputStream input = new DataInputStream(scan);
//
//                // write the Date to the socket
//                //InputStream scan2 = client2.getInputStream();
//               //DataInputStream input2 = new DataInputStream(scan2);
//              // Play(pout,pout2, input, input2);
//               PrintOutDateExchange(pout, input);  
//               client.close();
//               //PrintOutDateExchange(pout2, input2);
//            
//               //client2.close();
//
//        }     catch (Exception ioe) {
//            ioe.printStackTrace();
//        }
     }
    
//private static void Play(PrintWriter out1, PrintWriter out2, DataInputStream in1, DataInputStream in2){
//     // write the Date to the socket
//                out1.println(new java.util.Date().toString());
//                out2.println(new java.util.Date().toString());
//                
//                
//}
//public static void PrintOutDateExchange(PrintWriter print, DataInputStream data){
//                //server prints to client
//            print.println(new java.util.Date().toString());
//            // Server send the data to the client
//            try{
//            //data.readUTF();
//                System.out.println(data.read());
//                //System.out.println(read);
//            }catch(IOException ie){
//                System.err.println(ie);
//            }
//    
//}


}


