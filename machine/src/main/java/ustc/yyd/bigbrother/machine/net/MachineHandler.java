package ustc.yyd.bigbrother.machine.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import ustc.yyd.bigbrother.data.Message;
import ustc.yyd.bigbrother.data.MessageType;
import ustc.yyd.bigbrother.machine.MachineApplication;
import ustc.yyd.bigbrother.machine.util.Util;

import java.util.HashMap;

import static io.netty.handler.timeout.IdleState.WRITER_IDLE;

public class MachineHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        System.out.println("得到服务器数据");

        if(msg instanceof String){
            String s = (String)msg;
            Message message = JSON.parseObject(s, Message.class);
            System.out.println("数据内容："+JSONObject.toJSONString(message));
            switch (message.getType()){
                case telescreen_confirm_client:{//登记的回应
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
                    break;
                }
                case telescreen_changeClient_client:{//服务端命令客户端改变状态
                    String changeType = message.getContent().get("type");
                    switch (changeType){
                        case "delete":{//由于没有收到心跳，命令停止这个客户端
                            System.out.println("由于客户端没有收到心跳，停止这个客户端");
                            ctx.channel().close();
                            MachineApplication.run = false;
                            MachineApplication.client.group.shutdownGracefully();
                            System.out.println("请输入任意指令停止客户端");
                        }
                    }
                }
            }
        }
        else {
            System.out.println("not instanceof");
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{//4秒发一个心跳包
        if(evt instanceof IdleStateEvent){
            //System.out.println("触发空闲事件");
            IdleStateEvent event = (IdleStateEvent)evt;
            //System.out.println("空闲事件类型："+event.state());
            if(event.state()==WRITER_IDLE){
                //System.out.println("超时未发送心跳包，发一个心跳包");
                HashMap<String,String> content = new HashMap<>();
                String messageString = Util.creatMessageString(MessageType.client_heartBeat_telescreen,
                        content);
                ctx.channel().writeAndFlush(messageString);
                ctx.channel().writeAndFlush("\r\n");//根据\r\n进行换行
            }
        }
    }
}
