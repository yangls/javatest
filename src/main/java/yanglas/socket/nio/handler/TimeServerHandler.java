package yanglas.socket.nio.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimeServerHandler implements Runnable{

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;//从socket中读取
        PrintWriter out = null;//输出到socket

        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));//获取输入口
            out = new PrintWriter(this.socket.getOutputStream());//获取管道的输出口

            String currentTime = null;
            String body = null;
            while (true){
                body = in.readLine();
                if(body == null){
                    break;
                }
                System.out.println("the time server receive order:"+body);
                currentTime = "queryTime".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"bad Order";
                System.out.println("need out:"+currentTime);
                out.println(currentTime);

                //不关闭的话就还在内存不会写到流里，客户端就收不到
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            toClosed(in,out,socket);

        }
    }

    public static void toClosed(BufferedReader in, PrintWriter out, Socket socket) {
        if(in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            in = null;
        }
        if(out != null){
            out.close();
        }
        if(socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }
}
