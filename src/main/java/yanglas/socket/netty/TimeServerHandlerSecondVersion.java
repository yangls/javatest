package yanglas.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * ChannelHandlerAdapter 用于
 */
public class TimeServerHandlerSecondVersion extends ChannelHandlerAdapter {


    private int counter;

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

        String body = new String (req,"UTF-8").substring(0,req.length -System.getProperty("line.separator").length());

        //每读到一条消息后，计一次数目，发送应答给客户端
        System.out.println("the time server receive order :"+body +
        "; the counter is :"+ ++counter);

        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";

        currentTime = currentTime + System.getProperty("line.separator");

        ByteBuf res = Unpooled.copiedBuffer(currentTime.getBytes());
        //这里只是放到发送缓存数组中，只有flush后才会写入到socketChannel
        ctx.writeAndFlush(res);

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
