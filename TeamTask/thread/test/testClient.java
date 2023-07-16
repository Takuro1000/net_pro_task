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
            prReceive(inputStream, false);
            scSend(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSocket(socket, inputStream, outputStream);
        }
    }

    public static void receiveRoopStringflag(InputStream inputStream, String roopFlag) throws Exception {
        receiveRoopStringflag(inputStream, roopFlag, false);
    }

    public static void receiveRoopStringflag(InputStream inputStream, String roopFlag, boolean line) throws Exception {
        String message = null;
        while (true) {
            message = receive(inputStream);
            if (message == roopFlag) {
                break;
            } else if (line) {
                System.out.print(message);
            } else {
                System.out.println(message);
            }
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
    public static void prReceive(InputStream inputStream, boolean line) throws IOException {
        String message = receive(inputStream);
        if (message.isEmpty()) {
            prReceive(inputStream, line);
        } else {
            if (line) {
                System.out.print(message);
            } else {
                System.out.println(message);
            }
        }
    }

    public static void prReceive(InputStream inputStream) throws IOException {
        System.out.println(receive(inputStream));
    }

    public static String receive(InputStream inputStream) throws IOException {
        byte[] buff = new byte[1024];
        try {
            inputStream.read(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(buff, StandardCharsets.UTF_8);
    }

    // メッセージをサーバ側に送るメソッド
    public static void send(OutputStream outputStream, String message) throws IOException {
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