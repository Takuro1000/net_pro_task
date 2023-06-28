package TeamTask.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Server {

    public static void main(String[] args) throws IOException {
        new Thread(new ServerThread()).start();
            //スレッドのインスタンスを作成しRunnbleとしてServerThreadインスタンスを引数として渡し、スレッドを開始します
    }

    static class ServerThread implements Runnable {
        LinkedList<String> comments = new LinkedList<>();
        ServerSocketChannel serverChannel;
        Selector selector;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Random rand = new Random();

        public ServerThread() {
            try {
                serverChannel = ServerSocketChannel.open();
                    //ソケットチャンネルをオープン　ServerSocketChannel型のserverChannel変数の静的メソッドopen()
                serverChannel.socket().bind(new InetSocketAddress(10009));
                    //.socket()でserverChannelに関連したソケットを収得し、.bind()でソケットをローカルアドレスにバインド(関連付け)します
                    //new InetSocketAddress(port)はソケット・アドレスを作成します。この場合、IPアドレスはワイルドカード・アドレスで、ポート番号は指定された値です
                serverChannel.configureBlocking(false);
                    //チャンネルのブロックモードをfalseに設定（非ブロックモード）
                    //非ブロックモードにすることによりIOの処理を待機せずに以降の処理を行う
                selector = Selector.open(); //セレクタをオープンする
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);

                System.out.println("サーバーが起動しました。");
            } catch (IOException e) {
                System.out.println("サーバーの起動に失敗しました。");
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    selector.select();

                    Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                    while (selectedKeys.hasNext()) {
                        SelectionKey key = selectedKeys.next();
                        selectedKeys.remove();

                        if (!key.isValid()) {
                            continue;
                        }

                        if (key.isAcceptable()) {
                            accept(key);
                        } else if (key.isReadable()) {
                            receive(key);
                        }
                    }

                    broadcastComments();
                }
            } catch (IOException e) {
                System.out.println("サーバーでエラーが発生しました。");
                e.printStackTrace();
            } finally {
                try {
                    selector.close();
                    serverChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void accept(SelectionKey key) throws IOException {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("クライアントが接続しました。");
        }

        private void receive(SelectionKey key) throws IOException {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            buffer.clear();
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                // クライアントが切断された場合
                key.cancel();
                clientChannel.close();
                System.out.println("クライアントが切断されました。");
                return;
            }
            buffer.flip();
            String message = StandardCharsets.UTF_8.decode(buffer).toString();
            System.out.println("受信: " + message);
            comments.add(message);
        }

        private void broadcastComments() throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            String comment;
            while ((comment = comments.poll()) != null) {
                buffer.clear();
                buffer.put(comment.getBytes(StandardCharsets.UTF_8));
                buffer.flip();

                for (SelectionKey key : selector.keys()) {
                    if (key.isValid() && key.channel() instanceof SocketChannel) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        clientChannel.write(buffer);
                        buffer.rewind();
                    }
                }
            }
        }

    }
}
