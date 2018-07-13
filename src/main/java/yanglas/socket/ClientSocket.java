package yanglas.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientSocket {



    public static void main(String[] args){
        Socket client = null;
        PrintWriter out = null;
        BufferedReader reader = null;
        try {
            //创建请求
            client = new Socket();
            //链接
            client.connect(new InetSocketAddress("localhost",8000));

            //输出流
            out = new PrintWriter(client.getOutputStream(),true);
            out.println("hello world");
            //缓存清空，直接交付
            out.flush();

            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("server response:"+reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(reader!=null) reader.close();
                if(out!=null) out.close();
                if(client!=null) client.close();
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }

}
