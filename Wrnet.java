import java.net.*;
import java.io.*;

public class Wrnet {
    public static void main(String args[]){
        //実行引数関連
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        System.out.println("IPアドレス：" + args[0]);
        System.out.println("ポート番号：" + port);
        //変数定義
        boolean check = true;
        byte[] buff = new byte[1024];
        Socket writesocket = null;
        InputStream instr = null;
        OutputStream outstr = null; 
        //サーバーに接続
        try{
            writesocket = new Socket(ip, port);
            instr = writesocket.getInputStream();
            outstr= writesocket.getOutputStream();
        }catch(Exception e){
            System.out.println("ネットワークエラー: "+e);
            System.exit(1);
        }
        //繰り返し処理 check
        while(check){
            try{
                int n = System.in.read(buff);
                if(buff[0]=='S'&& buff[1]=='e'&& buff[2]=='n'&& buff[3]=='d'){
                    check = false;
                }
                else{
                    //writeメソッドでoutstrにbuffを格納
                    outstr.write(buff,0,n);
                }
            }catch(Exception e){
                System.out.println("入力エラーエラー: ");
                System.exit(1);
            }
        }
        //サーバーからの返事を出力
        try{
            int n = instr.read(buff);
            System.out.write(buff, 0, n);
        }catch(Exception e){
            check = false;
        }
        //instrを閉じる
        try{
            instr.close();
        }catch(Exception e){
            System.err.println("正常に終了できませんでした");
            System.exit(1);
        }
    }
}
