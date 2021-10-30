package ustc.yyd.bigbrother.telescreen.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ustc.yyd.bigbrother.data.Machine;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
*   Netty服务端启动类
*/

public class SocketServer {
    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;
    private static ServerBootstrap server;
    static Map<String, Channel> nameToChannel;//key是客户端名字，value是对应的channel
    static Map<Channel, String> channelToName;//key是channel，value是对应的客户端名字
    static List<Machine> machineList;//用于保存已经登记的machine，将来会用redis来代替

    static{
        nameToChannel = new ConcurrentHashMap<>();
        channelToName = new ConcurrentHashMap<>();
        machineList = new ArrayList<>();//实际上应该用线程安全的类

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MyChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

    }
    public static void run(int port){
        try {
            ChannelFuture future = server.bind(new InetSocketAddress(port)).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }
    }

}
