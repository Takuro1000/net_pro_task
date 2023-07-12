package TeamTask.channel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.io.BufferedReader;

public class WerewolfServer {

    private static final int PORT = 10009;

    public void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("サーバーが起動しました。");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("クライアントが接続しました。");

                // クライアントに役職を送信
                sendRoleToClient(clientSocket);

                // クライアントとの通信を別スレッドで処理
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("サーバーでエラーが発生しました。");
            e.printStackTrace();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRoleToClient(Socket clientSocket) throws IOException {
        // ここで役職の情報を生成・設定するロジックを実装する
        String role = generateRole(); // 役職を生成するメソッド例
        System.out.println(clientSocket.getLocalAddress() + "に役職を設定");
        // クライアントに役職を送信
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        writer.println(role);
    }

    private void handleClient(Socket clientSocket) {
        try {
            // クライアントとの通信処理を記述する
            // クライアントからのメッセージを受信し、適切な応答を返すなどの処理を行ってください
            // 以下は簡略化した例です

            // クライアントからのメッセージを受信するための準備
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // クライアントからのメッセージを受信し続ける
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("クライアントからのメッセージ: " + message);

                // メッセージに応じた処理を行う
                // 例えば、特定のメッセージを受信した場合に役職を確認するなどの処理を実装してください

                // 応答を送信
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println("サーバーからの応答: メッセージを受け取りました");
            }

            // クライアントとの接続を終了
            clientSocket.close();
            System.out.println("クライアントとの接続を終了しました。");

        } catch (IOException e) {
            System.out.println("クライアントとの通信中にエラーが発生しました。");
            e.printStackTrace();
        }
    }

    private String generateRole() {
        // Implement your logic to generate a role
        // For example, you can have a list of roles and randomly select one
        String[] roles = { "村人", "人狼", "占い師", "狂人" };
        Random rand = new Random();
        int index = rand.nextInt(roles.length);
        return roles[index];
    }

    public static void main(String[] args) {
        WerewolfServer server = new WerewolfServer();
        server.start();
    }
}
