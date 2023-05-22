import java.io.*;

public class Readwrite2 {
    public static void main(String args[]){
        byte[] buff = new byte[1024];
        boolean check = true;

        FileOutputStream outfile = null;
        try{
            outfile = new FileOutputStream(args[0]) ;
        } catch(FileNotFoundException e){
            System.err.println("ファイルがありません") ;
            System.exit(1) ;
        }


        while(check){
            try{
                int n = System.in.read(buff);
                System.out.println(n);
                System.out.write(buff, 0, n);
                for(int i = 0; i < n; i++){
                    System.out.println("Buff ["+i+"] = "+buff[i]);
                }
                if(buff[0] == '.'){
                    check = false;
                }
            } catch(Exception e){
                System.err.println("Err");
                System.exit(1);
            }
        }

    }
}
