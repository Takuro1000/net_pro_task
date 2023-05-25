import java.io.*;

public class Readfile2 {
    public static void main(String args[]){
        byte[] buff = new byte[1024];
        boolean check = true;
        FileInputStream infile = null;
        
        try{
            infile = new FileInputStream(args [0]);
        } catch (FileNotFoundException e){
            System.err.println("ファイルがありません");
            check = false;
        }

        while(check){
            try{
                int n = infile.read(buff);
                System.out.write(buff, 0, n);
                if(buff[0] == '.'){
                    break;
                } 
                for (int i=0;i < n;i++){
                    System.out.println("buff ["+i+"] = "+buff[i]);
                }
            } catch(Exception e){
                    System.err.println(e);
                    check = false;
            }
        }

        try {
            infile.close();
        } catch(IOException e){
            System.out.println("ファイル終了Err");
        }
    }
}