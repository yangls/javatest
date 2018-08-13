package yanglas.socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class TimeServer {

    public void bind(int port){
        //EventLoopGroup 线程组类，包含了一组NIO线程，专门用于网络事件的处理
        //实际上就是Reactor线程组。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //netty用于启动NIO服务端的辅助启动类，降低服务端的开发复杂度
        ServerBootstrap bootstrap = new ServerBootstrap();
        //配置辅助类
        bootstrap.group(bossGroup,workerGroup)//线程组作为入参传入到bootstrap
                .channel(NioServerSocketChannel.class)//类似于ServerSocketChannel
                .option(ChannelOption.SO_BACKLOG,1024)//channel的相关参数配置
                .childHandler(new ChildChannelHandler());//绑定io事件的处理类
        //绑定端口，同步等待成功
        try {
            //sync,阻塞方法，等待绑定端口完成
            ChannelFuture future = bootstrap.bind(port).sync();
            //等待服务器监听端口关闭，在服务端链路关闭之前，main函数不会退出
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //优雅退出
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
         //   socketChannel.pipeline().addLast(new TimeServerHandler());
            //粘包版本，只接收到2次
            socketChannel.pipeline().addLast(new TimeServerHandlerSecondVersion());
        }
    }

    public static void main(String[] args){
        int port= 8088;
        if(args != null && args.length>0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
        }
        new TimeServer().bind(port);

    }
}
