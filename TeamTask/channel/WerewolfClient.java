package TeamTask.channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class WerewolfClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 10009;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // サーバーから役職情報を受け取る
            String role = reader.readLine();
            System.out.println("あなたの役職は「" + role + "」です。");

            // ゲームのメインループ
            while (true) {
                System.out.println("メッセージを入力してください:");
                String message = reader.readLine();
                if (message.equalsIgnoreCase("quit")) {
                    break;
                }
                writer.println(message);
                writer.flush();

                // サーバーからの応答を受け取る
                String response = reader.readLine();
                System.out.println("サーバーからの応答: " + response);
            }

            // クライアントの終了処理
            socket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
