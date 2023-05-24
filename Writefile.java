import java.io.*;

public class Readwrite2 {
    public static void main(String args[]){
        byte[] buff = new byte[1024];
        boolean check = true;

        FileOutputStream outfile = null;
        try{
            outfile = new FileOutputStream(args[0]) ;
        } catch(FileNotFoundException e){
            System.err.println("ファイルがありません => ");
            check = false;
        }


        while(check){
            try{
                int n = System.in.read(buff);
                System.out.write(buff, 0, n);
                if(buff[0] == '.'){
                    check = false;
                }
                outfile.write(buff, 0, n);
                
            } catch(Exception e){
                System.err.println("ループErr");
                System.exit(1);
            }
        }

        try {
            outfile.close();
        } catch (IOException e) {
            System.out.println("ファイル終了Err => ");
        }
    }
}
