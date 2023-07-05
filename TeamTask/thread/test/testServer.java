package TeamTask.thread.test;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;

public class testServer {
    public static void main(String args[]) {
        int roop = 1;
        ArrayList<ServerThread> threads = new ArrayList<>();
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(10010);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            roop = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) { //NumberFormatException-文字列が解析可能な整数を含んでいない場合
            System.out.println("引数なしのため１回実行");
        }

        for (int i = 0; i < roop; i++) {
            try {
                Socket socket = serverSocket.accept();
                threads.add(new ServerThread(socket));
            } catch (IOException e) {
                System.err.println("サーバーの接続待機中にエラーが発生しました");
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
            sendln(socket, getName());
            closeSocket(socket);
        }

        public static void closeSocket(Socket socket){
            try{
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public static void send(Socket socket, String message) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(message.getBytes());
                outputStream.flush();
                System.out.println("送信成功: " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static void sendln(Socket socket, String message){
            send(socket, message+"\n");
        }

        public static String receive(Socket socket){    //クライアントからの入力を受け取ってStringをreturn
            byte[] buff = new byte[1024];
            InputStream inputStream = null;
            try{
                inputStream = socket.getInputStream();
            }catch(IOException e){
                e.printStackTrace();
            }

            try{
                inputStream.read(buff);
            }catch(IOException e){
                e.printStackTrace();
            }

            try{
                inputStream.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            return new String(buff, StandardCharsets.UTF_8);
        }
    }
}
