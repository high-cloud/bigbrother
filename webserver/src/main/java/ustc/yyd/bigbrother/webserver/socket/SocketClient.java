package ustc.yyd.bigbrother.webserver.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SocketClient {
    private static ChannelFuture future=null;

    private int port;//和服务器的哪个端口进行连接
    private String ip;//服务器ip


    public SocketClient(int port, String ip){
        this.ip = ip;
        this.port = port;

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.handler(new MyChannelInitalizer());
        try {
            future = bootstrap.connect(ip, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //用这种方式来实现向socket服务器写东西的具体逻辑
    public static void stopOneClient(String clientName){
        future.channel();
    }

}
