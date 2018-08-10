package yanglas.socket.nio.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * nio 客户端
 */
public class TimeClientHandle implements Runnable {

    private String host;

    private int port;

    private Selector selector;//多路选择器

    private SocketChannel socketChannel;//管道

    private volatile boolean stop = false;

    public TimeClientHandle(String host, int port) {
        this.host = host == null ? "127.0.0.1":host;
        this.port = port;

        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open();

            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void run() {
        System.out.println("------run---------");
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();//得到就绪态的keys
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    handleInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        System.out.println("------start handle input ---------");
        if(key.isValid()){//key可用
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if(key.isConnectable()){//可以连接 OP_CONNECT
                if(socketChannel.finishConnect()){
                    socketChannel.register(selector,SelectionKey.OP_READ);//链接上了，就注册到复用器， key.isReadable() == true，表示写入完毕，可以读了
                    doWrite(socketChannel);//写相关信息
                }else
                    System.exit(1);
            }
            //写了之后等待回应（读取相关管道返回数据）
            if(key.isReadable()){//准备就绪读取
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                int readByte = socketChannel.read(readBuffer);//管道读取到buffer，并得到数目

                if(readByte>0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body  = new String(bytes,"UTF-8");
                    //输出得到的服务端返回
                    System.out.println("Now is:"+body);
                    this.stop = true;
                }else if(readByte<0){
                    key.cancel();//链路关闭
                    socketChannel.close();//管道关闭
                }
            }
        }

    }

    private void doConnect() throws IOException {
        //如果直接连接成功，则注册到多路复用器上，发送请求信息，读应答
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector,SelectionKey.OP_READ);//管道注册读 isReadable( ) == true
            doWrite(socketChannel);
        }else{
            //否则去注册连接
            //当客户端调用connect()并注册OP_CONNECT事件后，连接操作就会就绪。
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        //要写的数据
        byte[] req = "QUERY TIME ORDER".getBytes();
        //缓存大小设置
        ByteBuffer writerBuffer = ByteBuffer.allocate(req.length);
        //放入相关数据
        writerBuffer.put(req);
        writerBuffer.flip();
        //管道写入,这样监听这个管道读的就可以读取了
        socketChannel.write(writerBuffer);
        if(!writerBuffer.hasRemaining()){
            System.out.println("Send order 2 server succeed.");
        }
    }
}
