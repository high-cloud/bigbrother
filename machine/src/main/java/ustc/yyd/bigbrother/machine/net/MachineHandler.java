package ustc.yyd.bigbrother.machine.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import ustc.yyd.bigbrother.data.Color;
import ustc.yyd.bigbrother.data.Machine;
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
                        System.out.println("注册失败，请稍后再试");
                        ctx.channel().close();
                        MachineApplication.run = false;
                        MachineApplication.client.group.shutdownGracefully();
                        System.out.println("请输入任意指令停止客户端");
                        break;
                    }
                    break;
                }
                case telescreen_changeClient_client:{//服务端命令客户端改变状态
                    String changeType = message.getContent().get("type");
                    switch (changeType){
                        case "stop":{//服务器命令终止或未收到心跳，命令停止这个客户端
                            System.out.println("服务器命令终止或未收到心跳，停止这个客户端");
                            ctx.channel().close();
                            MachineApplication.run = false;
                            MachineApplication.client.group.shutdownGracefully();
                            System.out.println("请输入任意指令停止客户端");
                            break;
                        }
                        case "update":{//收到修改客户端状态的指令
                            String machineString = message.getContent().get("machineObject");
                            Machine machine = JSON.parseObject(machineString, Machine.class);
                            //修改本地machine的值，不直接改变指针指向
                            MachineApplication.machine.setColor(machine.getColor());
                            MachineApplication.machine.setAutoChange(machine.isAutoChange());

                            // 报告新状态
                            SocketClient socketClient=MachineApplication.client;
                            socketClient.change(MachineApplication.machine);
//                            HashMap<String,String> content = new HashMap<>();
//                            content.put("type","update");
//                            content.put("machineObject",JSONObject.toJSONString(MachineApplication.machine));
//                            String messageString = Util.creatMessageString(MessageType.client_report_telescreen,
//                                    content);
//                            ctx.writeAndFlush(messageString);
//                            final ChannelFuture f= ctx.writeAndFlush("\r\n");//根据\r\n进行换行
//                            f.addListener(new ChannelFutureListener() {
//                                @Override
//                                public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                                    assert f==channelFuture;
//                                    System.out.println("finish report");
//                                    ctx.close();
//                                }
//                            });
                            break;
                        }
                    }//switch (changeType)
                    break;
                }//case telescreen_changeClient_client
            }//switch (message.getType())
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
                System.out.println("超时未发送心跳包，发一个心跳包");
                HashMap<String,String> content = new HashMap<>();
                String messageString = Util.creatMessageString(MessageType.client_heartBeat_telescreen,
                        content);
                ctx.channel().writeAndFlush(messageString);
                ctx.channel().writeAndFlush("\r\n");//根据\r\n进行换行
            }
        }
    }
}
