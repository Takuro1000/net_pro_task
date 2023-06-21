package TeamTask.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class Client {

    public static void main(String[] args) {
        try (
                SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 10009)); // ローカルで試す場合こっち
                // SocketChannel channel = SocketChannel.open(new
                // InetSocketAddress("192.168.111.26", 10009));//接続先指定
                Scanner sc = new Scanner(System.in);// 標準入力
        ) {

            System.out.println("送信する文字列を入力してEnterで送信します。ENDで終了します。");
            String text = null;

            // ENDが入力されるまで入力文字をサーバーに送信する
            while (!"END".equalsIgnoreCase(text = sc.next())) {
                ByteBuffer bb = StandardCharsets.UTF_8.encode(text);
                channel.write(bb);// 送信
                System.out.println("送信:" + text);
            }
        } catch (IOException e) {
            System.out.println("切断されました。");
            e.printStackTrace();
        }
    }
}