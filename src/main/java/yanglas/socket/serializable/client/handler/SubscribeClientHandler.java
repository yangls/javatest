package yanglas.socket.serializable.client.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import yanglas.socket.serializable.model.SubscribeReq;

public class SubscribeClientHandler extends ChannelHandlerAdapter {


    /**
     * 向服务器发送信息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //生成请求
        for(int i=0;i<10;i++){

            ctx.write(subReq(i));
        }
        //管道清空，确保向服务端发送
        ctx.flush();
    }

    /**
     * 接收服务器返回信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("resp:"+msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private SubscribeReq subReq(int i) {
        SubscribeReq req = new SubscribeReq();

        req.setId(i);
        req.setName("dear");
        req.setAddress("earth");
        req.setPhoneNumber("139********");
        req.setProductName("netty 教程");


        return req;
    }
}
