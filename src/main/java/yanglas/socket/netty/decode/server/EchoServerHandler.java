package yanglas.socket.netty.decode.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.util.concurrent.EventExecutorGroup;

/***
 * 继承了通道处理适配器
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

    int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //服务端的解码器会让msg会是完整的字符串数据包，直接强转
        String body = (String) msg;
        System.out.println("this is "+counter+" times receive client:"+body);
        body += "$_";//把过滤符加上，客户端也需要的分隔符号
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        //通道回写进去
        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

            //发生异常了就打印下，关闭通道

            cause.printStackTrace();
            ctx.close();
    }
}
