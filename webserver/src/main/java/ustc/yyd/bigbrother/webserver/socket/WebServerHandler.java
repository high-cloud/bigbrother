package ustc.yyd.bigbrother.webserver.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ustc.yyd.bigbrother.data.MessageType;
import ustc.yyd.bigbrother.webserver.util.Util;

import java.util.HashMap;

public class WebServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{//向socket服务器登记
        HashMap<String,String> content = new HashMap<>();
        content.put("name","@@");
        ctx.channel().writeAndFlush(Util.creatMessageString(MessageType.webserver_register_telescreen,
                content));
        ctx.channel().writeAndFlush("\r\n");//根据\r\n进行换行
    }
}
