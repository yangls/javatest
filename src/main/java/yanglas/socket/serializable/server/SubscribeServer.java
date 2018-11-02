package yanglas.socket.serializable.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import yanglas.socket.netty.decode.fixedlength.server.EchoServer;
import yanglas.socket.netty.decode.fixedlength.server.EchoServerHandler;
import yanglas.socket.serializable.server.handler.SubscribeServerHandler;

public class SubscribeServer {
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        new SubscribeServer().bind(port);
    }

    private void bind(int port) throws InterruptedException {

        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //增加解码器
                                socketChannel
                                        .pipeline()
                                        .addLast(new ObjectDecoder(1024*1024,
                                                ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())
                                ));

                                socketChannel.pipeline().addLast(new ObjectEncoder());
                                //在管道里增加处理器
                                socketChannel.pipeline().addLast(new SubscribeServerHandler());
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            //优雅退出
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
