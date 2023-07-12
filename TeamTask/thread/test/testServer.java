package TeamTask.thread.test;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.net.ServerSocket;
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;

public class testServer {
    public static void main(String args[]) {
        int playerNum = 0;
        ArrayList<ServerThread> threads = new ArrayList<>();
        ArrayList<Player> players = null;
        ServerSocket serverSocket = initializeServerSocket();

        try {
            playerNum = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) { // NumberFormatException-文字列が解析可能な整数を含んでいない場合
            e.printStackTrace();
            System.exit(1);
        }
        initializePlayerArray(players, playerNum);

        clientAccept(threads, playerNum, serverSocket);
        threadsStart(threads);

        threadsInterrupt(threads);
        closeServerSocket(serverSocket);
    }

    static void initializePlayerArray(ArrayList<Player> players, int playerNum) {
        players = new ArrayList<Player>(playerNum);
    }

    static void clientAccept(ArrayList<ServerThread> threads, int roop, ServerSocket serverSocket) {
        for (int i = 0; i < roop; i++) {
            try {
                Socket socket = serverSocket.accept();
                threads.add(new ServerThread(socket));
            } catch (IOException e) {
                System.err.println("サーバーの接続待機中にエラーが発生しました");
                e.printStackTrace();
            }
        }
        threads.trimToSize();
    }

    static void threadsStart(ArrayList<ServerThread> threads) {
        for (ServerThread thr : threads) {
            thr.start();
        }
    }

    static void threadsInterrupt(ArrayList<ServerThread> threads) {
        for (ServerThread thr : threads) {
            while (true) {
                if (thr.getState() == Thread.State.TIMED_WAITING) {
                    thr.interrupt();
                    break;
                }
            }
        }
    }

    static void closeServerSocket(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static ServerSocket initializeServerSocket() {
        int port = 10010;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }

    static class ServerThread extends Thread {
        Player player = null;
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        gamePhase phase = gamePhase.night;
        boolean game = true;

        ServerThread(Socket s) {
            this.socket = s;
        }

        ServerThread(Socket s, Player player) {
            this(s);
            this.player = player;
        }

        @Override
        public void run() {
            inputStream = initializeInputStream(socket);
            outputStream = initializeOutputStream(socket);
            setName(initializeUserName(outputStream, inputStream));
            game();
            closeSocket(socket);
        }

        public void game(){
            while(game){
                switch(phase){
                    case start:
                        wait10min();
                        game = false;
                    case day:
                    case night:
                        wait10min();
                        game = false;
                    case end:
                }
            }
        }

        public static InputStream initializeInputStream(Socket socket) {
            InputStream inputStream = null;
            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        }

        public static OutputStream initializeOutputStream(Socket socket) {
            OutputStream outputStream = null;
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return outputStream;
        }

        public static String initializeUserName(OutputStream outputStream, InputStream inputStream) {
            String username = null;
            send(outputStream, "接続されました名前を入れてください:");
            username = receive(inputStream).trim();
            System.out.println(username + "が接続しました。");
            return username;
        }

        public static void wait10min() {
            try {
                sleep(6000000);
            } catch (InterruptedException e) {
                return;
            }
        }

        public static void closeSocket(Socket socket) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void send(OutputStream outputStream, String message, String name) {
            try {
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
                System.out.println(name + " 送信成功: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static void send(OutputStream outputStream, String message) {
            send(outputStream, message, "");
        }
        public static void sendln(OutputStream outputStream, String message) {
            send(outputStream, message + "\n");
        }

        public static String receive(InputStream inputStream) { // クライアントからの入力を受け取ってStringをreturn
            byte[] buff = new byte[1024];

            try {
                inputStream.read(buff);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new String(buff, StandardCharsets.UTF_8);
        }

    }

    class Player {
        String playerName = null;
        boolean life = true;
        String role = null;

        Player() {

        }

        Player(String name, String role) {
            this.playerName = name;
            this.role = role;
        }
    }

    enum gamePhase{
        start,
        day,
        night,
        end;
    }
}
