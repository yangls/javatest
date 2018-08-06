package yanglas.socket.nio.aio.server;

import java.io.IOException;

public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port= 8088;
        if(args != null && args.length>0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
        }
        //异步服务器
        new Thread(new AsyncTimeServerHandler(port),"AIO-AsyncTimeServerHandler-001").start();

    }
}
