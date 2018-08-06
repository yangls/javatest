package yanglas.socket.nio.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandler implements Runnable{
    private int port;

    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    //利用它可以实现类似计数器的功能。比如有一个任务A，
    // 它要等待其他4个任务执行完毕之后才能执行，此时就可以利用CountDownLatch来实现这种功能了。
    CountDownLatch latch;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            //开启异步通道
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            //绑定监听端口
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("the time server is start in port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);//等待,完成操作之前将会一直阻塞

        doAccept();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {
        //
        asynchronousServerSocketChannel.accept(this,new AcceptCompletionHandler());
    }
}
