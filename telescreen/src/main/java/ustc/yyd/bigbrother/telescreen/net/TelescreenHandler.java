package ustc.yyd.bigbrother.telescreen.net;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.alibaba.fastjson.JSON;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.data.Message;
import ustc.yyd.bigbrother.data.MessageType;
import ustc.yyd.bigbrother.telescreen.util.Util;

import java.util.HashMap;


public class TelescreenHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        System.out.println("收到客户端数据");
        if(msg instanceof String){
            String s = (String)msg;
            Message message = JSON.parseObject(s.toString(), Message.class);
            System.out.println("数据内容："+JSONObject.toJSONString(message));
            switch (message.getType()){
                case client_register_telescreen:{
                    HashMap<String,String> content = message.getContent();
                    String machineObject = content.get("machineObject");
                    Machine machine = JSON.parseObject(machineObject, Machine.class);
                    System.out.println("machineName:"+machine.getName());
                    if(SocketServer.nameToChannel.containsKey(machine.getName())){//报告名字重复，将重新生成名字
                        //向客户端回复登记失败
                        HashMap<String,String> responseContent = new HashMap<>();
                        responseContent.put("result","fail");
                        ctx.channel().writeAndFlush(Util.creatMessageString(MessageType.telescreen_confirm_client,
                                responseContent));
                        ctx.channel().writeAndFlush("\r\n");//根据\r\n进行换行
                        break;
                    }
                    else {//成功登记
                        System.out.println("成功登记"+machine.getName()+"!");
                        SocketServer.nameToChannel.put(machine.getName(),ctx.channel());//将客户端名字和对应channel记录进Map
                        SocketServer.machineList.add(machine);//将客户端加入数据库

                        //向客户端回复登记成功
                        HashMap<String,String> responseContent = new HashMap<>();
                        responseContent.put("result","success");
                        ctx.channel().writeAndFlush(Util.creatMessageString(MessageType.telescreen_confirm_client,
                                responseContent));
                        ctx.channel().writeAndFlush("\r\n");//根据\r\n进行换行

                        break;
                    }

                }
            }
        }
        else{
            System.out.println("not instanceof");
        }

    }
}
