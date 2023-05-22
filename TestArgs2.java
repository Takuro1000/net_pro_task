import java.io.* ;
    public class TestArgs2{
    public static void main(String[] args){
        int port = Integer.parseInt(args[1]);
        System.out.println("IPアドレス：" + args[0]);
        System.out.println("ポート番号：" + port);
    }
}