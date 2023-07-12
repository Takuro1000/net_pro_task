package TeamTask.thread.test;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class testClient {
    public static void main(String args[]) {
        Socket socket = initializeSocket();
        InputStream inputStream = initializeInputStream(socket);
        OutputStream outputStream = initializeOutputStream(socket);
        try {
            receive(inputStream);
            scSend(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSocket(socket, inputStream, outputStream);
        }
    }

    public static OutputStream initializeOutputStream(Socket socket) {
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    public static InputStream initializeInputStream(Socket socket) {
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static Socket initializeSocket() {
        Socket socket = null;
        int port = 10010;
        String ip = "localhost";
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return socket;
    }

    public static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public static void closeSocket(Socket socket, InputStream inputStream, OutputStream outputStream) {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSocket(socket);
        }
    }

    // メッセージをサーバ側から受け取るメソッド
    public static void receive(InputStream inputStream) {
        byte[] buff = new byte[1024];

        try {
            int n = inputStream.read(buff);
            System.out.write(buff, 0, n);
        } catch (IOException e) {
            System.err.println("instr Exception");
            e.printStackTrace();
        }
    }

    // メッセージをサーバ側に送るメソッド
    public static void send(OutputStream outputStream, String message) {
        send(outputStream, message.getBytes(StandardCharsets.UTF_8));
    }

    public static void send(OutputStream outputStream, byte[] buff) {
        int n = buff.length;
        try {
            outputStream.write(buff, 0, n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void scSend(OutputStream outputStream) {
        byte[] buff = new byte[1024];
        try {
            System.in.read(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(outputStream, buff);
    }

}