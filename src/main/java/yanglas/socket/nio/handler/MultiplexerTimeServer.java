package yanglas.socket.nio.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用器类
 */
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;//多路选择器
    private ServerSocketChannel serverSocketChannel;//监听客户端的请求的管道

    private volatile boolean stop;

    /**
     * 资源初始化
     * 1.创建多路复用器
     * 2.创建服务管道
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            //选择器开启
            selector = Selector.open();
            //服务管道开启
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);//设置为非阻塞
            //管道绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
            //服务管道注册到选择器(监听accept)
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {
        //轮询遍历selector
        while(!stop){
            try {
                selector.select(1000);//一秒一发
                //获取select对应channel的key
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator it = selectionKeySet.iterator();
                SelectionKey key = null;
                while(it.hasNext()){
                     key = (SelectionKey) it.next();
                     it.remove();
                     handleInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
    //处理新接入的请求信息
    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            if(key.isAcceptable()){//测试这个key对应的channel是否准备好接受一个新的socket连接
                //接收新的连接请求
                //得到就绪的管道
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                //
                SocketChannel sc = ssc.accept();//完成三次握手
                sc.configureBlocking(false);//非阻塞
                sc.register(selector,SelectionKey.OP_READ);//建立了读取的物理链路（客户端要写了）

            }
            //能读数据
            if(key.isReadable()){//测试这个key对应的channel是否准备好读取
                //获取管道
                SocketChannel sc = (SocketChannel) key.channel();
                //无法知道客户端会发多少k内容来，先开个1k
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                //读取管道里的码流
                int readBytes = sc.read(readBuffer);
                //有数据
                if(readBytes>0){
                    //buffer reset
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String (bytes,"UTF-8");
                    System.out.println("the time server receive order:"+body);
                    //如果请求的指令是。。。就返回当前时间
                    //bussiness code
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
                    //写入管道,客户端将进行读取
                    doWrite(sc,currentTime);
                }else if(readBytes<0){
                    //链路已经关闭
                    key.cancel();
                    sc.close();
                }
            }

        }
    }

    private void doWrite(SocketChannel sc, String response) throws IOException {
        if(response != null && response.trim().length()>0){
            byte[] bytes = response.getBytes();//获取要写的内容
            ByteBuffer buffer  = ByteBuffer.allocate(bytes.length);//分配缓冲buffer大小
            buffer.put(bytes);
            buffer.flip();//清空缓存
            sc.write(buffer);//管道写入
        }
    }
}
