package yanglas.socket.nio;

import yanglas.socket.nio.handler.TimeServerHandler;
import yanglas.socket.nio.handler.TimeServerHandlerExecutePool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServerWithPool {

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
            //创建线程池，最大支持60个线程，10000个等待队列
            TimeServerHandlerExecutePool pool = new TimeServerHandlerExecutePool(50,10000);

            while (true){
                //阻塞直到收到链接生成socket
                //socket的输入（输出）流是阻塞方法，在读取（输出）完成之前会一直继续，读取（输出）慢的话，会一直阻塞
                //处理效率决定于io线程的处理速度和网络io的传输速度。
                socket = server.accept();
                //提交执行
                //这样的好处在于避免了每个请求都创建一个新的线程，因为pool和消息队列有界，从而避免了当线程过多导致的内存溢出
                pool.execute(new TimeServerHandler(socket));
            //    new Thread(new TimeServerHandler(socket)).start();
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
