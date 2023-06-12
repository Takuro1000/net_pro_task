import java.util.*;
import java.net.*;
import java.io.*;

public class Netclock {
    public static void main(String args[]){
        //変数宣言
        boolean check = true;
        ServerSocket servsocket = null;
        Socket socket = null;
        OutputStream out;
        String outstr;
        Date day;
        //servsocket作成
        try{
            servsocket = new ServerSocket(6000, 300);
            System.out.println("起動しました port = "+servsocket.getLocalPort());
        }catch(IOException e){
            System.err.println(e);
            System.exit(1);
        }
        //
        while(check){
            try{
                socket = servsocket.accept();
                System.out.println("接続されました:"+socket.getRemoteSocketAddress());
            
                day = new Date();
                outstr = "\n"
                        +"Hello, this is netclock server \n"
                        +day.toString()+"\n"
                        +"thank you";

                out = socket.getOutputStream();
                for(int i = 0; i < outstr.length(); i++){
                    out.write((int)outstr.charAt(i));
                }
                out.write('\n');
                socket.close();
            }catch(Exception e){
                System.err.println("ループ上のエラー\n"+e);
                System.exit(2);
            }
        }
    }
}
