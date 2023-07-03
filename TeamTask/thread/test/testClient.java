package TeamTask.thread.test;

import java.net.*;
import java.io.*;

public class testClient {
    static Socket socket = null;
    static InputStream instr = null;

    public static void main(String args[]){
        int port = 10010;
        String ip = "localhost";

        try{
            socket = new Socket(ip, port);
            instr = socket.getInputStream();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        client();
    }

    static void client(){
        byte[] buff = new byte[1024];
        boolean check = true;

        while(check){
            try{
                int n = instr.read(buff);
                System.out.write(buff, 0, n);
            }catch(IOException e){
                check = false;
            }
        }

        try{
            instr.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
