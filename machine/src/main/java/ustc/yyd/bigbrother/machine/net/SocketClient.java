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

import java.util.HashMap;


/*
    Netty客户端启动类
 */
public class SocketClient {
    private EventLoopGroup group =null;
    private Bootstrap client =null;
    public ChannelFuture future=null;

    private int port;//和服务器的哪个端口进行连接
    private String ip;//和服务器的哪个端口进行连接
    Machine machine = null;//记录这个channel对应的机器

    public SocketClient(Machine machine, int port, String ip){
        this.ip = ip;
        this.port = port;
        this.machine = machine;
        group = new NioEventLoopGroup();
        client = new Bootstrap();
        client.group(group);
        client.channel(NioSocketChannel.class);
        client.option(ChannelOption.SO_KEEPALIVE,true);
        client.handler(new MyChannelInitalizer());
        try {
            future = client.connect(ip, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message){
        try{
            System.out.println("客户端向服务端发送请求数据:"+ JSONObject.toJSONString(message));
            //客户端直接发送请求数据到服务端
            future.channel().writeAndFlush(JSONObject.toJSONString(message));
            //根据\r\n进行换行
            future.channel().writeAndFlush("\r\n");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        Message message = new Message();
        message.setType(MessageType.client_register_telescreen);

        HashMap<String,String> content = new HashMap<>();
        content.put("clientName",machine.getName());
        message.setContent(content);
        sendMessage(message);
    }
}
