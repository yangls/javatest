package yanglas.socket.netty.decode.fixedlength.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/***
 * 继承了通道处理适配器
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

    int counter = 0;

    /**
     * 读取管道信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         * 利用FixedLengthFrameDecoder 解码器，无论一次性收到多少数据报，都会按照固定长度进行解码，
         * 如果是半包信息，解码器会缓存半包信息并等待下个包到达后进行拼包，直至读取到一个完整的包
         */
        System.out.println("msg:"+msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

            //发生异常了就打印下，关闭通道

            cause.printStackTrace();
            ctx.close();
    }
}
