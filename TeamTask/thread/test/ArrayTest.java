package TeamTask.thread.test;

import java.util.ArrayList;

public class ArrayTest {
    public static void main(String args[]){
        int roop = 1;
        ArrayList<ServerThread> threads = new ArrayList<>();
        try {
            roop = Integer.parseInt(args[0]);
        } catch(Exception e) {
            System.out.println("引数なしのため１回実行");
        }
        for(int i = 0; i < roop; i++) {
            threads.add(new ServerThread());
        }
        for(ServerThread thr : threads) {
            thr.start();
        }
    }

    static class ServerThread extends Thread {
        @Override
        public void run() {
            System.out.println(getName());
        }
    }
}
