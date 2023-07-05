package TeamTask.thread.test;

import java.net.*;
import java.io.*;

class originalClient {
    public static void main(String args[]){
        int port = 10010;
        String ip = "localhost";

        try {
            Socket socket = new Socket(ip, port);
            receiveMessages(socket);
        } catch (IOException e) {
            System.out.println("ネットワークErr: " + e);
            System.exit(1);
        }
    }

    public static void receiveMessages(Socket socket) {
        byte[] buff = new byte[1024];
        boolean check = true;
        InputStream instr = null;

        try {
            instr = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            check = false;
        }

        while (check) {
            try {
                int n = instr.read(buff);
                if (n == -1) {
                    break; // 入力終了
                }
                System.out.write(buff, 0, n);
            } catch (IOException e) {
                e.printStackTrace();
                check = false;
            }
        }

        try {
            instr.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("正常に終了できませんでした");
            System.exit(1);
        }
    }
}
