package yanglas.socket.nio;

import yanglas.socket.nio.handler.TimeClientHandle;

public class TimeNIOClient {
    public static void main(String[] args){
        int port= 8088;
        if(args != null && args.length>0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
        }
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClientHandle-001").start();

    }
}
