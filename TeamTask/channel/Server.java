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
    }

    static class ServerThread implements Runnable {
        LinkedList<Comment> comments = new LinkedList<>();
        ServerSocketChannel serverChannel;
        Selector selector;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Random rand = new Random();

        public ServerThread() {
            try {
                serverChannel = ServerSocketChannel.open();
                serverChannel.socket().bind(new InetSocketAddress(10009));
                serverChannel.configureBlocking(false);

                selector = Selector.open();
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
            comments.add(new Comment(640, rand.nextInt(380) + 50, message));
        }

        private void broadcastComments() {
            Iterator<Comment> it = comments.iterator();
            while (it.hasNext()) {
                Comment comment = it.next();
                comment.x--;
                if (comment.x + calculateTextWidth(comment.comment) < 0) {
                    it.remove();
                }
            }
        }

        private int calculateTextWidth(String text) {
            // 仮の実装として、文字列の長さを幅として返す
            return text.length() * 10;
        }

        class Comment {
            int x;
            int y;
            String comment;

            Comment(int x, int y, String comment) {
                this.x = x;
                this.y = y;
                this.comment = comment;
            }
        }
    }
}