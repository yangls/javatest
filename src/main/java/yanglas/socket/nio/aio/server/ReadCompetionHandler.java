package yanglas.socket.nio.aio.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * 单次请求真正的处理方法（回调）
 */
public class ReadCompetionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel asynchronousSocketChannel;

    public ReadCompetionHandler(AsynchronousSocketChannel channel) {
        if(asynchronousSocketChannel == null)
            asynchronousSocketChannel = channel;

    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            byte[] body = new byte[attachment.remaining()];
            //从通道里读取参数（数据）
            attachment.get(body);

        try {
            String req = new String(body,"UTF-8");
            System.out.println("the time server receive order:"+req);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
            //执行写入操作
            doWriter(currentTime);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void doWriter(String currentTime) {
        if(currentTime != null && currentTime.trim().length()>0){
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writerBuffer = ByteBuffer.allocate(bytes.length);
            writerBuffer.put(bytes);
            writerBuffer.flip();
            //缓冲区，入参，结果回调处理类
            asynchronousSocketChannel.write(writerBuffer, writerBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    //如果没有完成，就接着发送
                    if(attachment.hasRemaining())
                        asynchronousSocketChannel.write(attachment,attachment,this);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        asynchronousSocketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        //发生了异常，一般会根据不同的异常做不同的处理
        //这里只是简单的关闭
        try {
            this.asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
