package yanglas.socket.nio;

import yanglas.socket.nio.handler.TimeServerHandler;

import java.io.*;
import java.net.Socket;

public class TimeClient {

    public static void main(String[] args){
        int port = 8088;

        if(args != null && args.length>0){
            port = Integer.valueOf(args[0]);
        }

        Socket socket = null;//客户端管道
        BufferedReader in = null;//socket的读入口
        PrintWriter out = null;//socket的输出口

        try {
            socket = new Socket("127.0.0.1",port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            out.println("queryTime");

            System.out.println("send order 2 server succeed");

            String resp = in.readLine();

            System.out.println("now is:"+resp);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            TimeServerHandler.toClosed(in,out,socket);
        }

    }
}
