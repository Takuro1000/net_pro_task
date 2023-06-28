package TeamTask.thread.test;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

public class testServer {
    public static void main(String args[]) {
        int roop = 1;
        ArrayList<ServerThread> threads = new ArrayList<>();
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(10010);
        } catch (IOException e) {
            System.out.println("サーバー作成中にエラーが発生しました");
            e.printStackTrace();
        }

        try {
            roop = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("引数なしのため１回実行");
        }

        for (int i = 0; i < roop; i++) {
            try {
                Socket socket = serverSocket.accept();
                threads.add(new ServerThread(socket));
            } catch (IOException e) {
                System.out.println("サーバーの接続待機中にエラーが発生しました");
                e.printStackTrace();
            }
        }
        for (ServerThread thr : threads) {
            thr.start();
        }
    }

    static class ServerThread extends Thread {
        Socket socket = null;

        ServerThread(Socket s) {
            this.socket = s;
        }

        @Override
        public void run() {
            send(socket, getName());
        }

        public static void send(Socket socket, String message) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(message.getBytes());
                outputStream.flush();
                System.out.println("送信成功: " + message);
            } catch (IOException e) {
                System.out.println("送信中にエラーが発生しました。");
                e.printStackTrace();
            }
        }
    }
}
