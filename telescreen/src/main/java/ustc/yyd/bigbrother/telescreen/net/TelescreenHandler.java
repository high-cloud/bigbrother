package ustc.yyd.bigbrother.telescreen.net;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.alibaba.fastjson.JSON;
import io.netty.handler.timeout.IdleStateEvent;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.data.Message;
import ustc.yyd.bigbrother.data.MessageType;
import ustc.yyd.bigbrother.telescreen.util.Util;

import java.util.HashMap;

import static io.netty.handler.timeout.IdleState.READER_IDLE;


public class TelescreenHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        if(msg instanceof String){
            String s = (String)msg;
            Message message = JSON.parseObject(s, Message.class);
            if(message.getType()!=MessageType.client_heartBeat_telescreen){//心跳包不显示
                System.out.println("数据内容："+JSONObject.toJSONString(message));
            }
            System.out.println("数据内容："+JSONObject.toJSONString(message));

            switch (message.getType()){
                case client_register_telescreen:{//服务器处理客户端登记请求
                    HashMap<String,String> content = message.getContent();
                    String machineObject = content.get("machineObject");
                    Machine machine = JSON.parseObject(machineObject, Machine.class);
                    if(SocketServer.nameToChannel.containsKey(machine.getName())
                            || !SocketServer.nameToChannel.containsKey("@@")){//报告名字重复或者web服务器没启动，登记失败
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
                        //SocketServer.machineMap.put(machine.getName(),machine);//将客户端加入数据库
                        SocketServer.channelToName.put(ctx.channel(),machine.getName());//将channel和对应的客户端名字记录进Map

                        //向web服务器通知有新的客户端接入
                        Channel webChannel= SocketServer.nameToChannel.get("@@");
                        HashMap<String,String> toWebContent = new HashMap<>();
                        toWebContent.put("name",machine.getName());
                        toWebContent.put("machineObject",machineObject);
                        webChannel.writeAndFlush(Util.creatMessageString(MessageType.telescreen_newClient_webserver,
                                toWebContent));
                        webChannel.writeAndFlush("\r\n");//根据\r\n进行换行

                        //向客户端回复登记成功
                        HashMap<String,String> responseContent = new HashMap<>();
                        responseContent.put("result","success");
                        ctx.channel().writeAndFlush(Util.creatMessageString(MessageType.telescreen_confirm_client,
                                responseContent));
                        ctx.channel().writeAndFlush("\r\n");//根据\r\n进行换行

                        break;
                    }
                }

                case client_report_telescreen:{//服务器收到客户端改变状态请求
                    HashMap<String,String> content = message.getContent();
                    String type = content.get("type");
                    String machineObject = content.get("machineObject");
                    Machine machine = JSON.parseObject(machineObject, Machine.class);

                    if(type.equals("stop")){//客户端主动关闭了，要在数据库中登记
                        String clientName = machine.getName();
                        //把这个断线的客户端从记录中删除
                        SocketServer.nameToChannel.remove(clientName);
                        SocketServer.channelToName.remove(ctx.channel());
                        //SocketServer.machineMap.remove(clientName);
                        System.out.println(clientName+"已经关闭");
                        ctx.channel().close();

                    }
                    else{//status.equals("update")  客户端更新了自己的状态
                        System.out.println(machine.getName()+"更新了状态");
                        //SocketServer.machineMap.put(machine.getName(),machine);

                    }

                    //向web服务器通知有客户端状态更改
                    if(SocketServer.nameToChannel.containsKey("@@")){//web服务器如果没启动，不用通知
                        Channel webChannel= SocketServer.nameToChannel.get("@@");
                        HashMap<String,String> toWebContent = new HashMap<>();
                        toWebContent.put("name",machine.getName());
                        toWebContent.put("type",type);
                        if("stop".equals(type)){
                            toWebContent.put("machineObject",null);
                        }
                        else{
                            toWebContent.put("machineObject",machineObject);
                        }

                        webChannel.writeAndFlush(Util.creatMessageString(MessageType.telescreen_clientChange_webserver,
                                toWebContent));
                        webChannel.writeAndFlush("\r\n");//根据\r\n进行换行
                    }
                }

                case client_heartBeat_telescreen:{//服务器收到心跳，暂时先不处理
                    //String clientName = SocketServer.channelToName.get(ctx.channel());
                    //System.out.println("收到"+clientName+"的心跳包");
                    break;
                }

                case webserver_register_telescreen:{//服务器处理webServer登记请求
                    SocketServer.nameToChannel.put("@@",ctx.channel());//将客户端名字和对应channel记录进Map
                    SocketServer.channelToName.put(ctx.channel(),"@@");
                    break;
                }
                case webserver_changeClient_telescreen:{//web服务器控制客户端状态改变
                    HashMap<String,String> content = message.getContent();
                    String type = content.get("type");
                    String machineName = content.get("name");
                    if("stop".equals(type)){//web服务器命令停止某个客户端
                        //向客户端报告，让它主动关闭
                        Channel clientChannel = SocketServer.nameToChannel.get(machineName);//通过map找到对应的channel
                        HashMap<String,String> responseContent = new HashMap<>();
                        responseContent.put("type","stop");
                        clientChannel.writeAndFlush(Util.creatMessageString(MessageType.telescreen_changeClient_client,
                                responseContent));
                        ChannelFuture future = ctx.channel().writeAndFlush("\r\n");//根据\r\n进行换行
                        future.channel().close();

                        SocketServer.channelToName.remove(ctx.channel());
                        SocketServer.nameToChannel.remove(machineName);
                        //SocketServer.machineMap.remove(machineName);
                    }
                    else{//type==update，要更新某个客户端状态
                        String machineObject = content.get("machineObject");
                        Machine machine = JSON.parseObject(machineObject, Machine.class);
                        //SocketServer.machineMap.put(machineName,machine);

                        System.out.println("telescreen_changeClient_client");
                        //向客户端报告，让它更新
                        Channel clientChannel = SocketServer.nameToChannel.get(machineName);//通过map找到对应的channel
                        HashMap<String,String> responseContent = new HashMap<>();
                        responseContent.put("type","update");
                        responseContent.put("machineObject",machineObject);
                        clientChannel.writeAndFlush(Util.creatMessageString(MessageType.telescreen_changeClient_client,
                                responseContent));
                        clientChannel.writeAndFlush("\r\n");//根据\r\n进行换行
                    }
                    break;
                }
            }
        }
        else{
            System.out.println("not instanceof");
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt){//5秒未收到任何消息，将会断掉连接
        if(evt instanceof IdleStateEvent){
            //System.out.println("触发空闲事件");
            IdleStateEvent event = (IdleStateEvent)evt;
            //System.out.println("空闲事件类型："+event.state());
            if(event.state()==READER_IDLE){
                String clientName = SocketServer.channelToName.get(ctx.channel());//得到断线的clientName
                //System.out.println("断线clientName:"+clientName);
                if("@@".equals(clientName)){//web服务器不用心跳
                    return;
                }
                //把这个断线的客户端从记录中删除
                SocketServer.nameToChannel.remove(clientName);
                SocketServer.channelToName.remove(ctx.channel());
                //SocketServer.machineMap.remove(clientName);
                System.out.println(clientName+"超时未发送心跳包，断掉连接");

                //向web服务器通告有客户端掉线
                Channel webChannel= SocketServer.nameToChannel.get("@@");
                HashMap<String,String> toWebContent = new HashMap<>();
                toWebContent.put("name",clientName);
                toWebContent.put("type","stop");
                toWebContent.put("machineObject",null);
                webChannel.writeAndFlush(Util.creatMessageString(MessageType.telescreen_clientChange_webserver,
                        toWebContent));
                webChannel.writeAndFlush("\r\n");//根据\r\n进行换行

                //如果链接还管用，就向客户端报告，让它主动关闭
                HashMap<String,String> responseContent = new HashMap<>();
                responseContent.put("type","stop");
                ctx.channel().writeAndFlush(Util.creatMessageString(MessageType.telescreen_changeClient_client,
                        responseContent));
                ChannelFuture future = ctx.channel().writeAndFlush("\r\n");//根据\r\n进行换行

                future.channel().close();
                ctx.disconnect();
            }
        }

    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String clientName = SocketServer.channelToName.get(ctx.channel());//得到断线的clientName
        System.out.println(clientName+"断开了连接");

        //向web服务器通告有客户端掉线
        Channel webChannel= SocketServer.nameToChannel.get("@@");
        HashMap<String,String> toWebContent = new HashMap<>();
        toWebContent.put("name",clientName);
        toWebContent.put("type","stop");
        toWebContent.put("machineObject",null);
        webChannel.writeAndFlush(Util.creatMessageString(MessageType.telescreen_clientChange_webserver,
                toWebContent));
        webChannel.writeAndFlush("\r\n");//根据\r\n进行换行

        SocketServer.nameToChannel.remove(clientName);
        SocketServer.channelToName.remove(ctx.channel());

        ctx.fireChannelInactive();
    }
}
