public class Readwrite {
    public static void main(String args[]){
        byte[] buff = new byte[1024];
        boolean check = true;

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
