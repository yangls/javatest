package yanglas.socket.serializable.server.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import yanglas.socket.serializable.model.SubscribeReq;
import yanglas.socket.serializable.model.SubscribeResp;

/**
 * 渠道处理适配器
 */
public class SubscribeServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //从管道里读取对象(已经有了加解码器在管道前置处理)
        SubscribeReq req = (SubscribeReq) msg;

        //逻辑处理
        if("dear".equalsIgnoreCase(req.getName())){
            System.out.println("req:"+req.toString());

            //处理完写入管道，返回客户端
            ctx.writeAndFlush(resp(req.getId()));
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();

    }


    private SubscribeResp resp(int id){
        SubscribeResp resp = new SubscribeResp();

        resp.setId(id);
        resp.setRespCode(0);
        resp.setDesc("order success");

        return resp;
    }
}
