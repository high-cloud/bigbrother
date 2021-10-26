package ustc.yyd.bigbrother.telescreen.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.alibaba.fastjson.JSON;
import ustc.yyd.bigbrother.data.Message;


public class TelescreenHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        if(msg instanceof String){
            String s = (String)msg;
            Message message = JSON.parseObject(s.toString(), Message.class);
            System.out.println(message.getContent().get("clientName"));
            System.out.println(message.getType());
        }
        else{
            System.out.println("not instanceof");
        }

    }
}
