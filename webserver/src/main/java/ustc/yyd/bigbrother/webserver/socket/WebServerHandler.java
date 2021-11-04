package ustc.yyd.bigbrother.webserver.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.data.Message;
import ustc.yyd.bigbrother.data.MessageType;
import ustc.yyd.bigbrother.webserver.data.DataBase;
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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        if(msg instanceof String){
            String s = (String)msg;
            Message message = JSON.parseObject(s, Message.class);
            HashMap<String,String> content = message.getContent();
            System.out.println("数据内容："+ JSONObject.toJSONString(message));
            switch (message.getType()){
                case telescreen_newClient_webserver:{//socket服务器告知web服务器有新客户端接入
                    String machineObject = content.get("machineObject");
                    Machine machine = JSON.parseObject(machineObject, Machine.class);
                    DataBase.machineMap.put(machine.getName(),machine);
                    break;
                }
                case telescreen_clientChange_webserver:{//socket服务器告知web服务器有客户端状态改变
                    String machineObject = content.get("machineObject");
                    String type = content.get("type");
                    String clientName = content.get("name");

                    if("update".equals(type)){
                        Machine machine = JSON.parseObject(machineObject, Machine.class);
                        DataBase.machineMap.put(clientName,machine);
                    }
                    else {//如果删除就在数据库中将其标记为离线
                        Machine machine = DataBase.machineMap.get(clientName);
                        machine.setOnline(false);
                        DataBase.machineMap.put(clientName,machine);
                    }

                    break;
                }
            }
        }
        else{
            System.out.println("not instanceof");
        }
    }
}
