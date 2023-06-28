package TeamTask.thread.test;

import java.net.*;
import java.io.*;

public class testClient {
    public static void main(String args[]){
        byte[] buff = new byte[1024];
        boolean check = true;
        int port = 10010;
        String ip = "localhost";
        Socket readsocket = null;
        InputStream instr = null;

        try{
            readsocket = new Socket(ip, port);
            instr = readsocket.getInputStream();
        }catch(Exception e){
            System.out.println("ネットワークErr: "+e);
            System.exit(1);
        }

        while(check){
            try{
                int n = instr.read(buff);
                System.out.write(buff, 0, n);
            }catch(Exception e){
                check = false;
            }
        }

        try{
            instr.close();
        }catch(Exception e){
            System.err.println("正常に終了できませんでした");
            System.exit(1);
        }
    }
}
