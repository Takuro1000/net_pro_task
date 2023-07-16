package TeamTask.thread.test;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.net.ServerSocket;
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.io.InputStream;

public class testServer {
    public static void main(String args[]) {
        int playerNum = 0;
        ArrayList<ServerThread> threads = new ArrayList<>();
        ServerSocket serverSocket = initializeServerSocket();
        gamePhase gPhase = gamePhase.end;

        try {
            playerNum = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(1);
        }

        clientAccept(threads, playerNum, serverSocket);
        threadsStart(threads);

        printAllPlayerName(threads);
        threadsInterrupt(threads, gPhase);
        closeServerSocket(serverSocket);
    }

    static void setRandomWolfRole(ArrayList<ServerThread> threads) {
        threadsCheckSleep(threads);
        Random random = new Random();
        int wolfIndex = random.nextInt(threads.size());
        threads.get(wolfIndex).setRole("人狼");
    }

    static void printAllPlayerName(ArrayList<ServerThread> threads) {
        threadsCheckSleep(threads);
        for (ServerThread thr : threads) {
            System.out.println(thr.getName());
        }
    }

    static void threadsCheckSleep(ArrayList<ServerThread> threads) {
        for (ServerThread thr : threads) {
            while (true) {
                if (thr.getState() == Thread.State.TIMED_WAITING) {
                    break;
                }
            }
        }
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

    static void threadsInterrupt(ArrayList<ServerThread> threads, gamePhase gamePhase) {
        for (ServerThread thr : threads) {
            threadsCheckSleep(threads);
            while (true) {
                if (thr.getState() == Thread.State.TIMED_WAITING) {
                    thr.phase = gamePhase;
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
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        public gamePhase phase = gamePhase.start;
        boolean game = true;
        boolean life = true;
        String role = "市民";

        ServerThread(Socket s) {
            this.socket = s;
        }

        @Override
        public void run() {
            inputStream = initializeInputStream(socket);
            outputStream = initializeOutputStream(socket);
            setName(initializeUserName(outputStream, inputStream));
            game();
            closeSocket(socket);
        }

        public void game() {
            while (game) {
                wait10min();
                switch (phase) {
                    case start:
                        System.err.println("not use Thread end");
                        game = false;
                        break;
                    case day:
                        System.out.println(phase.getString());
                        break;
                    case night:
                        System.out.println(phase.getString());
                        break;
                    case end:
                        System.out.println(phase.getString());
                        System.out.println(getName() + "は" + role + "でした");
                        game = false;
                        break;
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

        public void setRole(String role) {
            this.role = role;
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

        public void wait10min() {
            System.out.println(getName() + "割り込み待機（タイムアウト10分）");
            try {
                sleep(6000000);
            } catch (InterruptedException e) {
                return;
            }
        }

        public static void waitSec(int sec) {
            try {
                sleep(sec * 1000);
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

    enum gamePhase {
        start("phase:start"),
        day("phase:day"),
        night("phase:night"),
        end("phase:end");

        private final String string;

        private gamePhase(final String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }
}
