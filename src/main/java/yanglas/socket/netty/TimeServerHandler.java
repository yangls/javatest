package yanglas.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Date;

/**
 * ChannelHandlerAdapter 用于
 */
public class TimeServerHandler  extends ChannelHandlerAdapter {

    /**
     * 从管道读取出数据并进行业务处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //类似ByteBuffer 功能丰富
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        //写入req缓存
        buf.readBytes(req);

        String body = new String (req,"UTF-8");
        System.out.println("the time server receive order :"+body);

        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";

        ByteBuf res = Unpooled.copiedBuffer(currentTime.getBytes());
        //这里只是放到发送缓存数组中，只有flush后才会写入到socketChannel
        ctx.write(res);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将消息发送队列中的消息写入到SocketChannel，发送给对方
        ctx.flush();
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
