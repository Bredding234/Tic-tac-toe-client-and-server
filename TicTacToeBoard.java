/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tic_Tac_Toe_Server;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Brian
 */
public class TicTacToeBoard extends JFrame{
     private final String[] board = new String[169]; // tic-tac-toe board
    private final JTextArea outputArea; // for outputting moves
    private final Player[] players; // array of Players
    private ServerSocket server; // server socket to connect with clients
    private int currentPlayer; // keeps track of player with current move
    private final static int PLAYER_X = 0; // constant for first player
    private final static int PLAYER_O = 1; // constant for second player
    private final static String[] MARKS = {"X", "O"}; // array of marks
    private final ExecutorService runGame; // will run players
    private final Lock gameLock; // to lock game for synchronization
    private final Condition otherPlayerConnected; // to wait for other player
    private final Condition otherPlayerTurn; // to wait for other player's turn

    // set up tic-tac-toe server and GUI that displays messages
    public TicTacToeBoard() {
        super("Tic-Tac-Toe Server"); // set title of window

        // created a ExecutorService with a thread for each player
        runGame = Executors.newFixedThreadPool(2);
        gameLock = new ReentrantLock(); // create lock for game

        // condition variable for both players being connected
        otherPlayerConnected = gameLock.newCondition();

        // condition variable for the other player's turn
        otherPlayerTurn = gameLock.newCondition();

        for (int i = 0; i < 169; i++) {
            board[i] = ""; // create tic-tac-toe board
        }
        players = new Player[2]; // create array of players
        currentPlayer = PLAYER_X; // set current player to first player

        try {
            server = new ServerSocket(12345, 2); // set up ServerSocket
        } catch (IOException ioException) {
            System.out.println(ioException.toString());
            System.exit(1);
        }

        outputArea = new JTextArea(); // create JTextArea for output
        add(outputArea, BorderLayout.CENTER);
        outputArea.setText("Server awaiting connections\n");
        setSize(300, 300); // set size of window
        setVisible(true); // show window
    }

    // wait for two connections so game can be played
    public void execute() {
        // wait for each client to connect
        for (int i = 0; i < players.length; i++) {
            // wait for connection, create Player, start runnable
            try {
                /**
                 * When a client connects, a new Player object is created to
                 * manage the connection as a separate thread, and the thread is
                 * executed in the runGame thread pool.
                 *
                 * The Player constructor receives the Socket object
                 * representing the connection to the client and gets the
                 * associated input and output streams.
                 */
                players[i] = new Player(server.accept(), i);
                runGame.execute(players[i]); // execute player runnable
            } catch (IOException ioException) {
                System.out.println(ioException.toString());
                System.exit(1);
            }
        }

        gameLock.lock(); // lock game to signal player X's thread

        try {
            players[PLAYER_X].setSuspended(false); // resume player X
            otherPlayerConnected.signal(); // wake up player X's thread
        } finally {
            gameLock.unlock(); // unlock game after signalling player X
        }
    }

    // display message in outputArea
    private void displayMessage(final String messageToDisplay) {
        // display message from event-dispatch thread of execution
        SwingUtilities.invokeLater(() -> {
            // updates outputArea
            outputArea.append(messageToDisplay); // add message
        });
    }

    // determine if move is valid
    public boolean validateAndMove(int location, int player) {
        // while not current player, must wait for turn
        while (player != currentPlayer) {
            gameLock.lock(); // lock game to wait for other player to go

            try {
                otherPlayerTurn.await(); // wait for player's turn
            } catch (InterruptedException exception) {
                System.out.println(exception.toString());
            } finally {
                gameLock.unlock(); // unlock game after waiting
            }
        }

        // if location not occupied, make move
        if (!isOccupied(location)) {
            board[location] = MARKS[currentPlayer]; // set move on board
            currentPlayer = (currentPlayer + 1) % 2; // change player

            // let new current player know that move occurred
            players[currentPlayer].otherPlayerMoved(location);

            gameLock.lock(); // lock game to signal other player to go

            try {
                otherPlayerTurn.signal(); // signal other player to continue
            } finally {
                gameLock.unlock(); // unlock game after signaling
            }

            return true; // notify player that move was valid
        } else {
            // move was not valid
            return false; // notify player that move was invalid
        }
    }

    // determine whether location is occupied
    public boolean isOccupied(int location) {
        return board[location].equals(MARKS[PLAYER_X]) || board[location].equals(MARKS[PLAYER_O]);
    }

    /**
     * Check if there is 13 of the same marks in a row.
     *
     * @return True if there is a winner, false if there is not a winner.
     */
    public boolean hasWinner() {
        return (!board[0].isEmpty() && board[0].equals(board[1]) && board[0].equals(board[2])&& board[0].equals(board[3])&& board[0].equals(board[4])&& board[0].equals(board[5])&& board[0].equals(board[6])&& board[0].equals(board[7])&& board[0].equals(board[8])&& board[0].equals(board[9])&& board[0].equals(board[10])&& board[0].equals(board[11])&& board[0].equals(board[12]))
             
                // Each of the 12 rows
                || (!board[13].isEmpty() && board[13].equals(board[14]) && board[13].equals(board[15])&& board[13].equals(board[16])&& board[13].equals(board[17])&& board[13].equals(board[18])&& board[13].equals(board[19])&& board[13].equals(board[20])&& board[13].equals(board[21])&& board[13].equals(board[22])&& board[13].equals(board[23])&& board[13].equals(board[24])&& board[13].equals(board[25]))
                || (!board[26].isEmpty() && board[26].equals(board[27]) && board[26].equals(board[28])&& board[26].equals(board[29])&& board[26].equals(board[30])&& board[26].equals(board[31])&& board[26].equals(board[32])&& board[26].equals(board[33])&& board[26].equals(board[34])&& board[26].equals(board[35])&& board[26].equals(board[36])&& board[26].equals(board[37])&& board[26].equals(board[38]))
                ||(!board[39].isEmpty() && board[39].equals(board[40]) && board[39].equals(board[41])&& board[39].equals(board[42])&& board[39].equals(board[43])&& board[39].equals(board[44])&& board[39].equals(board[45])&& board[39].equals(board[46])&& board[39].equals(board[47])&& board[39].equals(board[48])&& board[39].equals(board[49])&& board[39].equals(board[50])&& board[39].equals(board[51]))
                || (!board[52].isEmpty() && board[52].equals(board[53]) && board[52].equals(board[54])&& board[52].equals(board[55])&& board[52].equals(board[56])&& board[52].equals(board[57])&& board[52].equals(board[58])&& board[52].equals(board[59])&& board[52].equals(board[60])&& board[52].equals(board[61])&& board[52].equals(board[62])&& board[52].equals(board[63])&& board[52].equals(board[64]))
                || (!board[65].isEmpty() && board[65].equals(board[66]) && board[65].equals(board[67])&& board[65].equals(board[68])&& board[65].equals(board[69])&& board[65].equals(board[70])&& board[65].equals(board[71])&& board[65].equals(board[72])&& board[65].equals(board[73])&& board[65].equals(board[74])&& board[65].equals(board[75])&& board[65].equals(board[76])&& board[65].equals(board[77]))
                || (!board[78].isEmpty() && board[78].equals(board[79]) && board[78].equals(board[80])&& board[78].equals(board[81])&& board[78].equals(board[82])&& board[78].equals(board[83])&& board[78].equals(board[84])&& board[78].equals(board[85])&& board[78].equals(board[86])&& board[78].equals(board[87])&& board[78].equals(board[88])&& board[78].equals(board[89])&& board[78].equals(board[90]))
                || (!board[91].isEmpty() && board[91].equals(board[92]) && board[91].equals(board[93])&& board[91].equals(board[94])&& board[91].equals(board[95])&& board[91].equals(board[96])&& board[91].equals(board[97])&& board[91].equals(board[98])&& board[91].equals(board[99])&& board[91].equals(board[100])&& board[91].equals(board[101])&& board[91].equals(board[102])&& board[91].equals(board[103]))
                || (!board[104].isEmpty() && board[104].equals(board[105]) && board[104].equals(board[106])&& board[104].equals(board[107])&& board[104].equals(board[108])&& board[104].equals(board[109])&& board[104].equals(board[110])&& board[104].equals(board[111])&& board[104].equals(board[112])&& board[104].equals(board[113])&& board[104].equals(board[114])&& board[104].equals(board[115])&& board[104].equals(board[116]))
                || (!board[117].isEmpty() && board[117].equals(board[118]) && board[117].equals(board[119])&& board[117].equals(board[120])&& board[117].equals(board[121])&& board[117].equals(board[122])&& board[117].equals(board[123])&& board[117].equals(board[124])&& board[117].equals(board[125])&& board[117].equals(board[126])&& board[117].equals(board[127])&& board[117].equals(board[128])&& board[117].equals(board[129]))
                || (!board[130].isEmpty() && board[130].equals(board[131]) && board[130].equals(board[132])&& board[130].equals(board[133])&& board[130].equals(board[134])&& board[130].equals(board[135])&& board[130].equals(board[136])&& board[130].equals(board[137])&& board[130].equals(board[138])&& board[130].equals(board[139])&& board[130].equals(board[140])&& board[130].equals(board[141])&& board[130].equals(board[142]))
                || (!board[143].isEmpty() && board[143].equals(board[144]) && board[143].equals(board[145])&& board[143].equals(board[146])&& board[143].equals(board[147])&& board[143].equals(board[148])&& board[143].equals(board[149])&& board[143].equals(board[150])&& board[143].equals(board[151])&& board[143].equals(board[152])&& board[143].equals(board[153])&& board[143].equals(board[154])&& board[143].equals(board[155]))
                || (!board[156].isEmpty() && board[156].equals(board[157]) && board[156].equals(board[158])&& board[156].equals(board[159])&& board[156].equals(board[160])&& board[156].equals(board[161])&& board[156].equals(board[162])&& board[156].equals(board[163])&& board[156].equals(board[164])&& board[156].equals(board[165])&& board[156].equals(board[166])&& board[156].equals(board[167])&& board[156].equals(board[168]))
                
                
               
                || (!board[0].isEmpty() && board[0].equals(board[14]) && board[0].equals(board[28])&& board[0].equals(board[42])&& board[0].equals(board[56])&& board[0].equals(board[70])&& board[0].equals(board[84])&& board[0].equals(board[98])&& board[0].equals(board[112])&& board[0].equals(board[126])&& board[0].equals(board[140])&& board[0].equals(board[154])&& board[0].equals(board[168])) // The first diagonal in first position
                || (!board[12].isEmpty() && board[12].equals(board[24]) && board[12].equals(board[36])&& board[12].equals(board[48])&& board[12].equals(board[60])&& board[12].equals(board[72])&& board[12].equals(board[84])&& board[12].equals(board[108])&& board[12].equals(board[120])&& board[12].equals(board[132])&& board[12].equals(board[144])&& board[12].equals(board[156])) // second diagonal in the 12th position
               
                // Each of the 12 columns 
                || (!board[0].isEmpty() && board[0].equals(board[13]) && board[0].equals(board[26])&& board[0].equals(board[39])&& board[0].equals(board[52])&& board[0].equals(board[65])&& board[0].equals(board[78])&& board[0].equals(board[91])&& board[0].equals(board[104])&& board[0].equals(board[117])&& board[0].equals(board[130])&& board[0].equals(board[143])&& board[0].equals(board[156]))
               ||(!board[1].isEmpty() && board[1].equals(board[14]) && board[1].equals(board[27])&& board[1].equals(board[40])&& board[1].equals(board[53])&& board[1].equals(board[66])&& board[1].equals(board[79])&& board[1].equals(board[92])&& board[1].equals(board[105])&& board[1].equals(board[118])&& board[1].equals(board[131])&& board[1].equals(board[144])&& board[1].equals(board[157]))
                ||(!board[2].isEmpty() && board[2].equals(board[15]) && board[2].equals(board[28])&& board[2].equals(board[41])&& board[2].equals(board[54])&& board[2].equals(board[67])&& board[2].equals(board[80])&& board[2].equals(board[93])&& board[2].equals(board[106])&& board[2].equals(board[119])&& board[2].equals(board[132])&& board[2].equals(board[145])&& board[2].equals(board[158]))
                || (!board[3].isEmpty() && board[3].equals(board[16]) && board[3].equals(board[29])&& board[3].equals(board[42])&& board[3].equals(board[55])&& board[3].equals(board[68])&& board[3].equals(board[81])&& board[3].equals(board[94])&& board[3].equals(board[107])&& board[3].equals(board[120])&& board[3].equals(board[133])&& board[3].equals(board[146])&& board[3].equals(board[159]))
                || (!board[4].isEmpty() && board[4].equals(board[17]) && board[4].equals(board[30])&& board[4].equals(board[43])&& board[4].equals(board[56])&& board[4].equals(board[69])&& board[4].equals(board[82])&& board[4].equals(board[95])&& board[4].equals(board[108])&& board[4].equals(board[121])&& board[4].equals(board[134])&& board[4].equals(board[147])&& board[4].equals(board[160]))
                || (!board[5].isEmpty() && board[5].equals(board[18]) && board[5].equals(board[31])&& board[5].equals(board[44])&& board[5].equals(board[57])&& board[5].equals(board[70])&& board[5].equals(board[83])&& board[5].equals(board[96])&& board[5].equals(board[109])&& board[5].equals(board[122])&& board[5].equals(board[135])&& board[5].equals(board[148])&& board[5].equals(board[161]))
                || (!board[6].isEmpty() && board[6].equals(board[19]) && board[6].equals(board[32])&& board[6].equals(board[45])&& board[6].equals(board[58])&& board[6].equals(board[71])&& board[6].equals(board[84])&& board[6].equals(board[97])&& board[6].equals(board[110])&& board[6].equals(board[123])&& board[6].equals(board[136])&& board[6].equals(board[149])&& board[6].equals(board[162]))
                || (!board[7].isEmpty() && board[7].equals(board[20]) && board[7].equals(board[33])&& board[7].equals(board[46])&& board[7].equals(board[59])&& board[7].equals(board[72])&& board[7].equals(board[85])&& board[7].equals(board[98])&& board[7].equals(board[111])&& board[7].equals(board[124])&& board[7].equals(board[137])&& board[7].equals(board[150])&& board[7].equals(board[163]))
                || (!board[8].isEmpty() && board[8].equals(board[21]) && board[8].equals(board[34])&& board[8].equals(board[47])&& board[8].equals(board[60])&& board[8].equals(board[73])&& board[8].equals(board[86])&& board[8].equals(board[99])&& board[8].equals(board[112])&& board[8].equals(board[125])&& board[8].equals(board[138])&& board[8].equals(board[151])&& board[8].equals(board[164]))
                || (!board[9].isEmpty() && board[9].equals(board[22]) && board[9].equals(board[35])&& board[9].equals(board[48])&& board[9].equals(board[61])&& board[9].equals(board[74])&& board[9].equals(board[87])&& board[9].equals(board[100])&& board[9].equals(board[113])&& board[9].equals(board[126])&& board[9].equals(board[139])&& board[9].equals(board[152])&& board[9].equals(board[165]))
                || (!board[10].isEmpty() && board[10].equals(board[23]) && board[10].equals(board[36])&& board[10].equals(board[49])&& board[10].equals(board[62])&& board[10].equals(board[75])&& board[10].equals(board[88])&& board[10].equals(board[101])&& board[10].equals(board[114])&& board[10].equals(board[127])&& board[10].equals(board[140])&& board[10].equals(board[153])&& board[10].equals(board[166]))
                || (!board[11].isEmpty() && board[11].equals(board[24]) && board[11].equals(board[37])&& board[11].equals(board[50])&& board[11].equals(board[63])&& board[11].equals(board[76])&& board[11].equals(board[89])&& board[11].equals(board[102])&& board[11].equals(board[115])&& board[11].equals(board[128])&& board[11].equals(board[141])&& board[11].equals(board[154])&& board[11].equals(board[167]))
                || (!board[12].isEmpty() && board[12].equals(board[25]) && board[12].equals(board[38])&& board[12].equals(board[51])&& board[12].equals(board[64])&& board[12].equals(board[77])&& board[12].equals(board[90])&& board[12].equals(board[103])&& board[12].equals(board[116])&& board[12].equals(board[129])&& board[12].equals(board[142])&& board[12].equals(board[155])&& board[12].equals(board[168])
                || (!board[0].isEmpty() && board[0].equals(board[12]) && board[0].equals(board[24])&& board[0].equals(board[36])&& board[0].equals(board[48])&& board[0].equals(board[60])&& board[0].equals(board[72])&& board[0].equals(board[84])&& board[0].equals(board[96])&& board[0].equals(board[108])&& board[0].equals(board[120])&& board[0].equals(board[132])));
    }

    /**
     * Check if the game board is full.
     *
     * @return True if the board is full, false if there is an empty slot.
     */
    public boolean boardFilledUp() {
        for (int i = 0; i < board.length; ++i) {
            if (board[i].isEmpty()) {
                return false;
            }
        }

        return true;
    }

    // place code in this method to determine whether game over 
    public boolean isGameOver() {
        return hasWinner() || boardFilledUp();
    }

    // private inner class Player manages each Player as a runnable
    private class Player implements Runnable {

        private final Socket connection; // connection to client
        private Scanner input; // input from client
        private Formatter output; // output to client
        private final int playerNumber; // tracks which player this is
        private final String mark; // mark for this player
        private boolean suspended = true; // whether thread is suspended

        // set up Player thread
        public Player(Socket socket, int number) {
            playerNumber = number; // store this player's number
            mark = MARKS[playerNumber]; // specify player's mark
            connection = socket; // store socket for client

            // obtain streams from Socket
            try {
                input = new Scanner(connection.getInputStream());
                output = new Formatter(connection.getOutputStream());
            } catch (IOException ioException) {
                System.out.println(ioException.toString());
                System.exit(1);
            }
        }

        // send message that other player moved
        public void otherPlayerMoved(int location) {
            output.format("Opponent moved\n");
            output.format("%d\n", location); // send location of move
            output.flush(); // flush output
            output.format(hasWinner() ? "DEFEAT\n" : boardFilledUp() ? "TIE\n" : "");
            output.flush();
        }

        // control the thread's execution
        @Override
        public void run() {
            // send client its mark (X or O), process messages from client
            try {
                displayMessage("Player " + mark + " connected\n");
                output.format("%s\n", mark); // send player's mark
                output.flush(); // flush output

                /**
                 * Wait until both players are connected
                 */
                // if player X, wait for another player to arrive
                if (playerNumber == PLAYER_X) {
                    output.format("%s\n%s", "Player X connected", "Waiting for another player\n");
                    output.flush(); // flush output
                    gameLock.lock(); // lock game to  wait for second player

                    try {
                        while (suspended) {
                            otherPlayerConnected.await(); // wait for player O
                        }
                    } catch (InterruptedException exception) {
                        System.out.println(exception.toString());
                    } finally {
                        gameLock.unlock(); // unlock game after second player
                    }

                    // send message that other player connected
                    output.format("Other player connected. Your move.\n");
                    output.flush(); // flush output
                } else {
                    output.format("Player O connected, please wait\n");
                    output.flush(); // flush output
                }

                // while game not over
                while (!isGameOver()) {
                    int location = 0; // initialize move location

                    if (input.hasNext()) {
                        location = input.nextInt(); // get move location
                    }
                    // check for valid move
                    if (validateAndMove(location, playerNumber)) {
                        displayMessage("\nlocation: " + location);
                        output.format("Valid move.\n"); // notify client
                        output.flush(); // flush output
                        output.format(hasWinner() ? "VICTORY\n" : boardFilledUp() ? "TIE\n" : "");
                        output.flush();
                    } else {
                        // move was invalid
                        output.format("Invalid move, try again\n");
                        output.flush(); // flush output
                    }
                }
            } finally {
                try {
                    connection.close(); // close connection to client
                } catch (IOException ioException) {
                    System.out.println(ioException.toString());
                    System.exit(1);
                }
            }
        }

        // set whether or not thread is suspended
        public void setSuspended(boolean status) {
            suspended = status; // set value of suspended
        }
    }
 

} // end class TicTacToeServer
 


