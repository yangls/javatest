package yanglas.socket.nio;

import yanglas.socket.nio.handler.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            System.out.println("the Time server is start in port:"+port);
            Socket socket = null;
            while (true){
                //阻塞直到收到链接生成socket
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(server != null){
                System.out.println("time server close");
                server.close();
                server = null;
            }
        }
    }
}
