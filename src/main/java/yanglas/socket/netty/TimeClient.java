package yanglas.socket.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

    public void connect(int port,String host){
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
                        socketChannel.pipeline().addLast(new TimeClientHandler());
                    }
                });
            //异步连接
            ChannelFuture f = b.connect(host,port).sync();
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();

        }
    }
    public static void main(String[] args) {
        int port = 8088;

        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        new TimeClient().connect(port,"127.0.0.1");
    }
}
