package ustc.yyd.bigbrother.telescreen.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/*
    用于配置channel的Handler
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()));
        ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
        //ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));//心跳Handler放在这
        ch.pipeline().addLast(new TelescreenHandler());  //业务逻辑Handler放在这
        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
    }
}
