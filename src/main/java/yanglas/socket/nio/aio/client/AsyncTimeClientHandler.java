package yanglas.socket.nio.aio.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandler  implements Runnable, CompletionHandler<Void, AsyncTimeClientHandler> {
    private String host;
    private int port;

    AsynchronousSocketChannel clientChannel;

    CountDownLatch countDownLatch;

    public AsyncTimeClientHandler(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            clientChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        //建立连接，附件，处理类
        clientChannel.connect(new InetSocketAddress(host,port),this,this);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AsyncTimeClientHandler attachment) {
        //连接建立后执行查询
        byte[] req = "QUERY TIME ORDER".getBytes();

        //缓存写入
        ByteBuffer buffer = ByteBuffer.allocate(req.length);
        buffer.put(req);
        buffer.flip();

        //使用的缓存，写操作的入参，回调匿名内部类
        clientChannel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer responseByteBuffer) {
                //如果还有数据（没传完）
                if(responseByteBuffer.hasRemaining()){
                    clientChannel.write(responseByteBuffer,responseByteBuffer,this);
                }else{
                    //读取得到的结果
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    //使用的缓存，读操作的入参，回调匿名内部类
                    clientChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer responseReadByteBuffer) {
                            responseReadByteBuffer.flip();
                            byte[] bytes = new byte[responseReadByteBuffer.remaining()];
                            responseReadByteBuffer.get(bytes);
                            String body;
                            try {
                                body = new String(bytes,"UTF-8");
                                System.out.println("Now is:"+body);
                                countDownLatch.countDown();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer responseReadByteBuffer) {
                            try {
                                clientChannel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer responseByteBuffer) {
                try {
                    clientChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
