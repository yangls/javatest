package yanglas.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandlerSecondVersion extends ChannelHandlerAdapter {

   // private final ByteBuf firstMsg;

    private int counter;

    private byte[] req;

    public TimeClientHandlerSecondVersion() {
        req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
       // firstMsg = Unpooled.buffer(req.length);
       // firstMsg.writeBytes(req);
    }

    /**
     * 服务端返回应答信息，读取
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
         ByteBuf buf = (ByteBuf) msg;
         byte[] req = new byte[buf.readableBytes()];
         buf.readBytes(req);
//         String body = new String(req,"UTF-8");
        //利用LineBasedFrameDecoder和StringDecoder 后无需编码
        String body = (String) msg;

        System.out.println("Now is:"+body +"; the counter is :"+ ++counter);
    }

    /**
     * 客户端和服务端TCP链路建立成功以后，netty的NIO线程会调用这个方法
     * 负责发送buf的信息到服务端
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       // ctx.writeAndFlush(firstMsg);
        ByteBuf message = null;
        for(int i=0;i<100;i++){
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    /**
     * 如果处理异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
