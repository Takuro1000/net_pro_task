package TeamTask.thread.test;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class testClient {
    public static void main(String args[]) {
        int port = 10010;
        String ip = "localhost";
        Socket socket = null;

        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        receive(socket);
        scSend(socket);
        closeSocket(socket);
    }

    public static void closeSocket(Socket socket){
        try{
            socket.close();
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            System.exit(0);
        }
    }

    // メッセージをサーバ側から受け取るメソッド
    public static void receive(Socket socket) {
        byte[] buff = new byte[1024];
        InputStream instr = null;

        try {
            instr = socket.getInputStream();
            int n = instr.read(buff);
            System.out.write(buff, 0, n);
        } catch (IOException e) {
            System.err.println("instr Exception");
            e.printStackTrace();
        }
    }

    // メッセージをサーバ側に送るメソッド
    public static void send (Socket socket,String message){
        send(socket, message.getBytes());
    }
    public static void send (Socket socket,byte[] buff){
        OutputStream outstr = null;
        int n = buff.length;
        try {
            outstr = socket.getOutputStream();
            outstr.write(buff,0,n);
        }catch (IOException e){
            e.printStackTrace();
        }        
    }
    public static void scSend(Socket socket){
        byte[] buff = new byte[1024];
        try{
            System.in.read(buff);
        }catch(IOException e){
            e.printStackTrace();
        }
        send(socket, buff);
    }

}