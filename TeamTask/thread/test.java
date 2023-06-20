package TeamTask.thread;

public class test {
    public static void main(String[] args){
        MyThread t = new MyThread();
        MyRunnable r = new MyRunnable();
        Thread rt = new Thread(r);
        t.start();
        rt.start();
    }
}

class MyThread extends Thread{
    public void run(){
        System.out.println("This thread is "+getName());
    }
}

class MyRunnable implements Runnable{
    public void run(){
        String threadName = Thread.currentThread().getName();
        System.out.println("MyRunnble を使ってThreadを動かしてます "+threadName);
    }
}