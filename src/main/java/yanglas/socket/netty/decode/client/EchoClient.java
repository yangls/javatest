package yanglas.socket.netty.decode.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {

    public static void main(String[] agrs){
        int port =8080;
        new EchoClient().connect("127.0.0.1",port);
    }

    private void connect(String s, int port) {
        //事件线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //启动辅助类
            Bootstrap b = new Bootstrap();

            //init 这里的channel就不是服务的channel了
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //channelPipeline 放入业务处理类，处理io事件
                            //  socketChannel.pipeline().addLast(new TimeClientHandler());
                            //设定分隔符
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            //配置解码器（单条最大长度，如果到1024都没有分隔符就抛出异常）
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                            //配置字符解码器
                            socketChannel.pipeline().addLast(new StringDecoder());
                            //这个版本，客户端只会收到2次返回，发生了粘包。客户端是发送了100次。
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //异步连接
            ChannelFuture f = b.connect(s,port).sync();
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();

        }
    }
}
