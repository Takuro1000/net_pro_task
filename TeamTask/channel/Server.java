package TeamTask.channel;

import java.awt.Graphics;
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

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Server {

    public static void main(String[] args) throws IOException {
        new Thread(new TestWindow()).start();
    }

    static class TestWindow extends JFrame implements Runnable {
        LinkedList<Comment> comments = new LinkedList<>();
        ServerSocketChannel channel;
        Selector sel;
        ByteBuffer bb = ByteBuffer.allocate(1024);
        Random rand = new Random();

        public TestWindow() {
            setSize(640, 480);
            add(new DrawCanvas());
            setVisible(true);
            initServer();
        }

        // サーバーソケットの開始
        void initServer() {
            try {
                channel = ServerSocketChannel.open();
                channel.socket().bind(new InetSocketAddress(10009));// 受信ポートを指定
                channel.configureBlocking(false);
                sel = Selector.open();
                // 接続要求を監視
                channel.register(sel, SelectionKey.OP_ACCEPT);
            } catch (IOException e) {
                System.out.println("サーバー開始エラー");
                e.printStackTrace();
            }
        }

        // 接続受け付けメソッド
        void accept() {
            try {
                SocketChannel sc = channel.accept();
                if (sc == null)
                    return;
                // 非ブロックモードで接続リストに追加
                sc.configureBlocking(false);
                // 受信を監視
                sc.register(sel, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // データ受信メソッド
        void recv(SocketChannel sc) {
            try {
                // 接続していないなら閉じる
                if (!sc.isConnected()) {
                    System.out.println("接続終了");
                    sc.close();
                }
                // バイナリ受信領域初期化
                bb.clear();

                // 受信
                sc.read(bb);

                // ロード準備
                bb.flip();

                // 文字列に変換
                String result = StandardCharsets.UTF_8.decode(bb).toString();
                if (result.length() > 0) {
                    System.out.println("受信:" + result);
                    // ランダムな位置でメッセージを出現させる
                    comments.add(new Comment(640, rand.nextInt(380) + 50, result));
                }
            } catch (IOException e) {
                try {
                    System.out.println("接続終了");
                    sc.close();
                } catch (IOException e1) {
                }
            }
        }

        // 通信処理
        void networkLogic() {
            try {
                // イベントが発生している場合は処理させます
                while (sel.selectNow() > 0) {
                    Iterator<SelectionKey> it = sel.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        if (key.isAcceptable()) {
                            accept();
                        } else if (key.isReadable()) {
                            recv((SocketChannel) key.channel());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // アニメーションループ
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(40);
                    networkLogic();
                    repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // 描画キャンバス
        class DrawCanvas extends JPanel {
            // 描画
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Iterator<Comment> it = comments.iterator();
                while (it.hasNext()) {
                    Comment comment = it.next();
                    g.drawString(comment.comment, comment.x, comment.y);
                    if (comment.x + g.getFontMetrics().stringWidth(comment.comment) < 0) {
                        it.remove();
                    } else {
                        comment.x--;
                    }
                }
            }
        }

        // メッセージ情報
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