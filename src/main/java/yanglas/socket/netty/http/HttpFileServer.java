package yanglas.socket.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import yanglas.socket.netty.http.handler.HttpFileServerHandler;

public class HttpFileServer {

    private static final String DEFAULT_URL = "/src/com/yls/netty";

    public void run(final int port,final String url ){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //各式http协议的解码器增加
                        ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                        //将多个消息转换为单一的FullHttpRequest或者FullHttpResponse
                        ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                        ch.pipeline().addLast("http-encoder",new HttpResponseDecoder());
                        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                        ch.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
                    }
                });


            ChannelFuture future = b.bind("192.168.15.63",port).sync();
            System.out.println("HTTP 文件服务器启动，网址是192.168.15.63"+port+url);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


    public static void main(String[] args){
        int port = 8080;
        if(args.length>0){
            port = Integer.parseInt(args[0]);

        }
        String url = DEFAULT_URL;
        if(args.length>1){
            url = args[1];

        }
        new HttpFileServer().run(port,url);
    }
}
