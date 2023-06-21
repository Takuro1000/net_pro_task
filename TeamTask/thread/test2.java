package TeamTask.thread;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class test2 {
    public static final int PORT = 10007;

    public static void main(String args[]) {
        int numberOfPeople = 0;
        Socket socket = null;
        ServerSocket serverSocket = null;
        ArrayList<Socket> sockets = new ArrayList<Socket>();
        try {
            numberOfPeople = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.err.println("第一引数に参加人数を入れてください\n" + e);
            System.exit(1);
        }
        ServerThread[] serverThreads = new ServerThread[numberOfPeople];
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("サーバー:mainが起動しました port" + serverSocket.getLocalPort());
            System.out.println("参加人数は " + numberOfPeople + "人です。");
            for (int i = 0; i < numberOfPeople; i++) {
                socket = serverSocket.accept();
                sockets.add(i, socket);
            }
            for (int i = 0;i < sockets.size();i++){
                serverThreads[i].setSocket(sockets.get(i));
                serverThreads[i].run();
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
            }
        }
    }
}

class ServerThread extends Thread{
    private Socket socket;

    public void run(){
        stringOutToSocket("クライアントはスレッドに接続できました");
    }

    public void stringOutToSocket(String str){
        try{
            byte[] strByte = new byte[1024];
            OutputStream out = socket.getOutputStream();
            strByte = str.getBytes(StandardCharsets.UTF_8);
            out.write(strByte);
        }catch(IOException e){
            System.err.println(e);
        }
    }

    public void setSocket(Socket s){
        socket = s;
        System.out.println("接続されました"+socket.getRemoteSocketAddress());
    }
}