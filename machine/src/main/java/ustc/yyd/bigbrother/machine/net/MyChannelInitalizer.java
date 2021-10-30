package ustc.yyd.bigbrother.machine.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/*
    用于配置channel的Handler
 */
public class MyChannelInitalizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //按照\r\n进行解码
        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()));
        ch.pipeline().addLast(new StringDecoder());
        ch.pipeline().addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));//心跳Handler
        ch.pipeline().addLast(new MachineHandler());//业务逻辑handler
        ch.pipeline().addLast(new StringEncoder());
    }
}
