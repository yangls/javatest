package yanglas.socket.nio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadNIOEchoServer {
    //nio 的选择器。处理所有的网络链接
    private Selector selector;
    /**
     * 线程池
     */
    private static ExecutorService tp = Executors.newCachedThreadPool();

    /**
     * socket处理的具体业务逻辑
     * 读取socket并返回
     */
    static class HandleMsg implements Runnable{
        Socket clientSocket;

        public HandleMsg(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            BufferedReader is = null;//输入流
            PrintWriter os = null;//输出流
            System.out.println("------------process----------");
            try{
                //初始化对客户端输入输出流获取
                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String inputLine = null;
                long b = System.currentTimeMillis();
                inputLine = is.readLine();
                System.out.println("string:"+inputLine);
                while( inputLine != null){
                    //模拟业务完成输出
                    os.println(inputLine+"s");
                }
                long e = System.currentTimeMillis();
                System.out.println("spend times: "+(e-b)+" ms");
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                //执行完毕关闭流
                try {
                    if(is!=null) is.close();
                    if(os!=null) os.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        //服务创建
        try {
            //8000端口等待请求
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //服务运行
        while(true){
            try {
                //接收到的socket链接
                clientSocket = serverSocket.accept();
                System.out.println("client:"+clientSocket.getRemoteSocketAddress()+" connect!");
                //执行线程处理
                tp.execute(new HandleMsg(clientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
