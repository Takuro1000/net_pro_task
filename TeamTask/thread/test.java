package TeamTask.thread;

public class test {
    public static void main(String[] args){
        MyRunnable r = new MyRunnable();
        MyThread t = new MyThread();
        Thread rt = new Thread(r);
        t.start();
        rt.start();
    }
}

class MyThread extends Thread{
    public void run(){
        Citizen c = new Citizen();
        System.out.println("This thread is "+getName()+c.getMyrole());
    }
}

class MyRunnable implements Runnable{
    public void run(){
        Werewolf w = new Werewolf();
        String threadName = Thread.currentThread().getName();
        System.out.println("MyRunnble を使ってThreadを動かしてます "+threadName+w.getMyrole());
    }
}

class Citizen {
    private String myrole = null;
    private boolean life = true;
    Citizen(){
        myrole = "市民";
    }
    Citizen(String r){
        myrole = r;
    }
    void Die(){
        life = false;
    }
    String getMyrole(){
        return myrole;
    }
}

class Werewolf extends Citizen{
    Werewolf(){
        super("人狼");
    }
}