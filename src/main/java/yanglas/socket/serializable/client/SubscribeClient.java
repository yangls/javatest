package yanglas.socket.serializable.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import yanglas.socket.serializable.client.handler.SubscribeClientHandler;

public class SubscribeClient {

    public static void main(String[] agrs){
        int port =8080;
        new SubscribeClient().connect("127.0.0.1",port);
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
                            //增加解码器
                            socketChannel
                                    .pipeline()
                                    .addLast(new ObjectDecoder(1024,
                                            ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())
                                    ));

                            socketChannel.pipeline().addLast(new ObjectEncoder());
                            //在管道里增加处理器
                            socketChannel.pipeline().addLast(new SubscribeClientHandler());
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
