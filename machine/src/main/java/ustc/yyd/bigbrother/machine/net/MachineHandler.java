package ustc.yyd.bigbrother.machine.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ustc.yyd.bigbrother.data.Message;
import ustc.yyd.bigbrother.data.MessageType;
import ustc.yyd.bigbrother.machine.MachineApplication;
import ustc.yyd.bigbrother.machine.util.Util;

import java.util.HashMap;

public class MachineHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        System.out.println("得到服务器数据了");
        if(msg instanceof String){
            String s = (String)msg;
            Message message = JSON.parseObject(s, Message.class);
            switch (message.getType()){
                case telescreen_confirm_client:{

                    if(message.getContent().get("result").equals("success")){//注册成功
                        System.out.println("已成功注册"+ MachineApplication.machine.getName());
                    }
                    else{//注册失败，重新生成名字，重新初始化
                        System.out.println("由于有同名服务器，注册失败，重新生成服务器名后重试");
                        MachineApplication.machine.setName(Util.randomName(3));
                        System.out.println("新服务器名为"+MachineApplication.machine.getName());

                        //重新向服务器发送登记请求
                        HashMap<String,String> content = new HashMap<>();
                        content.put("machineObject", JSONObject.toJSONString(MachineApplication.machine));
                        String messageString = Util.creatMessageString(MessageType.client_register_telescreen,
                                content);
                        ctx.channel().writeAndFlush(messageString);
                        ctx.channel().writeAndFlush("\r\n");//根据\r\n进行换行
                    }

                }
            }
        }
        else {
            System.out.println("not instanceof");
        }
    }
}
