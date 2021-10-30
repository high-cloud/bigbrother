package ustc.yyd.bigbrother.machine.net;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.data.Message;
import ustc.yyd.bigbrother.data.MessageType;
import ustc.yyd.bigbrother.machine.MachineApplication;
import ustc.yyd.bigbrother.machine.util.Util;

import java.util.HashMap;


/*
    Netty客户端启动类
 */
public class SocketClient {
    EventLoopGroup group =null;
    private ChannelFuture future=null;

    private int port;//和服务器的哪个端口进行连接
    private String ip;//服务器ip


    public SocketClient(int port, String ip){
        this.ip = ip;
        this.port = port;
        group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
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

//    public void sendMessage(Message message){
//        try{
//            System.out.println("客户端向服务端发送请求数据:"+ JSONObject.toJSONString(message));
//            //客户端直接发送请求数据到服务端
//            future.channel().writeAndFlush(JSONObject.toJSONString(message));
//            //根据\r\n进行换行
//            future.channel().writeAndFlush("\r\n");
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

    public void init(Machine machine){//向服务器发送登记信息
        HashMap<String,String> content = new HashMap<>();
        content.put("machineObject",JSONObject.toJSONString(machine));
        String messageString = Util.creatMessageString(MessageType.client_register_telescreen,
                content);
        future.channel().writeAndFlush(messageString);
        future.channel().writeAndFlush("\r\n");//根据\r\n进行换行
    }
}
