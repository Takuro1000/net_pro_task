package TeamTask.thread;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class server {
    public static void main(String[] args){
        //変数宣言
        boolean check = true;
        ServerSocket servsocket = null;
        Socket socket = null;
        int player = 0;
        int flag=0;
        //人狼宣言
        try{
            player = Integer.parseInt(args[0]);
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
}

class ServRunnble implements Runnable{
    Socket socket = null;
    citizen role = null;
    OutputStream out = null;
    String outstr = null;
    ServRunnble(Socket s,int roleflag){
        try{
            socket = s;
        }catch(Exception e){
            System.err.println(e);
            System.exit(1);
        }
        if(roleflag==1){
            role = new werewolf();
        }else{
            role = new citizen();
        }
    }
    public void run(){
        try{
            //役職伝達
            outstr = "\n"
                    +"こんにちはあなたの役職は、\n"
                    +role.getRole()+" です。"+"\n";

            out = socket.getOutputStream();
            for(int i = 0; i < outstr.length(); i++){
                out.write(stringToBytes(outstr));
            }
        }catch(Exception e){
            System.out.println(e);
            System.exit(1);
        }
    }
    public static byte[] stringToBytes(String str) {
    return str.getBytes(StandardCharsets.UTF_8);
    }
}

class citizen {
    String myrole = null;
    citizen(){
        myrole = "市民";
    }
    citizen(String r){
        myrole = r;
    }
    String getRole(){
        return myrole;
    }
}

class werewolf extends citizen{
    werewolf(){
        super("人狼");
    }
}