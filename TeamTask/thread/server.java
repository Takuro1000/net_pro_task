package TeamTask.thread;

import java.util.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class server {
    public static void main(String[] args){
        //変数宣言
        Random random = new Random();
        boolean check = true;
        ServerSocket servsocket = null;
        Socket socket = null;
        OutputStream out;
        String outstr;
        int player = 0;
        int flag=0;
        String[] role = null;
        //人狼宣言
        try{
            player = Integer.parseInt(args[0]);
            int randomNum = random.nextInt(player);
            role = new String[player];
            for(int i = 0; i < player; i++){
                if(i==randomNum){
                    role[i]="人狼";
                }
                else{
                    role[i]="市民";
                }
            }
        }catch(Exception e){
            System.err.println("引数がありません"+'\n'+e);
            System.exit(1);
        }
        //servsocket作成
        try{
            servsocket = new ServerSocket(6000, 300);
            System.out.println("起動しました port = "+servsocket.getLocalPort());
            System.out.println("参加人数は："+player+"人です。");
        }catch(IOException e){
            System.err.println(e);
            System.exit(1);
        }
        //接続後処理
        while(check){
            try{
                socket = servsocket.accept();
                System.out.println("接続されました:"+socket.getRemoteSocketAddress());
                //役職伝達
                outstr = "\n"
                        +"こんにちはあなたの役職は、\n"
                        +role[flag]+" です。";

                out = socket.getOutputStream();
                for(int i = 0; i < outstr.length(); i++){
                    out.write(stringToBytes(outstr));
                }
                out.write('\n');
                socket.close();
                flag++;
                //サーバー終了
                if(flag==player){
                    check=false;
                    System.out.println("全員参加しました。");
                }
            }catch(Exception e){
                System.err.println("ループ上のエラー\n"+e);
                System.exit(2);
            }
        }
        //正常終了
        System.exit(0);
    }

    public static byte[] stringToBytes(String str) {
    return str.getBytes(StandardCharsets.UTF_8);
    }
}