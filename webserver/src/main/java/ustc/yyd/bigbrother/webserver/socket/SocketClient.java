package ustc.yyd.bigbrother.webserver.socket;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.data.MessageType;
import ustc.yyd.bigbrother.webserver.util.Util;

import java.util.HashMap;

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

    //向socket服务器发停止命令
    public static void stopOneClient(String clientName){
        HashMap<String,String> content = new HashMap<>();
        content.put("name",clientName);
        content.put("type","stop");

        future.channel().writeAndFlush(Util.creatMessageString(MessageType.webserver_changeClient_telescreen,
                content));
        future.channel().writeAndFlush("\r\n");

    }

    //向socket服务器发改变状态命令
    public static void changeOneClient(String clientName, Machine machine){
        HashMap<String,String> content = new HashMap<>();
        content.put("name",clientName);
        content.put("type","update");
        content.put("machineObject", JSONObject.toJSONString(machine));

        System.out.println("changeOneClient");
        future.channel().writeAndFlush(Util.creatMessageString(MessageType.webserver_changeClient_telescreen,
                content));
        future.channel().writeAndFlush("\r\n");
    }

}
