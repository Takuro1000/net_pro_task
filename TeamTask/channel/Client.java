package TeamTask.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 10009;
    public static void main(String[] args) {
        try (SocketChannel channel = SocketChannel.open(new InetSocketAddress(SERVER_IP, SERVER_PORT));
            Scanner sc = new Scanner(System.in)) {

            System.out.println("サーバーに接続しました。");
            System.out.println("名前を入力してください:");
            String name = sc.nextLine();

            System.out.println("送信する文字列を入力してEnterで送信します。ENDで終了します。");
            // サーバーからの応答を表示するスレッドを開始
            ReadClientThread readThread = new ReadClientThread(channel);
            readThread.start();

            String text = null;

            // END が入力されるまで入力文字列をサーバーに送信する
            while (!"END".equalsIgnoreCase(text = sc.next())) {
                ByteBuffer buffer = StandardCharsets.UTF_8.encode(name + ": " + text);
                channel.write(buffer); // 送信
                // System.out.println("送信: " + text);
                buffer.clear();
            }
        } catch (IOException e) {
            System.out.println("サーバーへの接続中にエラーが発生しました。");
            e.printStackTrace();
        }
    }

    static class ReadClientThread extends Thread {
        private SocketChannel channel;

        public ReadClientThread(SocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void run() {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (channel.read(buffer) != -1) {
                    buffer.flip();
                    String message = StandardCharsets.UTF_8.decode(buffer).toString();
                    System.out.println(message);
                    buffer.clear();
                }
            } catch (IOException e) {
                System.out.println("サーバーからの受信中にエラーが発生しました。");
                e.printStackTrace();
            }
        }
    }
}
