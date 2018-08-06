package yanglas.socket.nio.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 客户端的接收后的处理方法
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel,AsyncTimeServerHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
        //获取服务的channel，调用accept方法。
        //当asynchronousServerSocketChannel的accpt方法接收成功后，会回调这个方法，表示新的客户端已经接入成功
        //之所以要重新调用accept，是因为asynchronousServerSocketChannel可以接收很多客户端，需要继续调用accept，接收其他的连接，形成循环
        attachment.asynchronousServerSocketChannel.accept(attachment,this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //接收缓冲区，异步Channel携带的附件（通知回调的入参），接收通知回调的业务handler
        result.read(buffer,buffer,new ReadCompetionHandler(result));
     }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();//完成了之后就降低1
    }
}
