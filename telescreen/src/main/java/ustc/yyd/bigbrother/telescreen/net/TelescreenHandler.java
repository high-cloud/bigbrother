package ustc.yyd.bigbrother.telescreen.net;

import com.alibaba.fastjson.JSONObject;
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
            Message message = JSON.parseObject(s.toString(), Message.class);
            if(message.getType()!=MessageType.client_heartBeat_telescreen){//心跳包不显示
                System.out.println("数据内容："+JSONObject.toJSONString(message));
            }

            switch (message.getType()){
                case client_register_telescreen:{//服务器处理客户端登记请求
                    HashMap<String,String> content = message.getContent();
                    String machineObject = content.get("machineObject");
                    Machine machine = JSON.parseObject(machineObject, Machine.class);
                    //System.out.println("machineName:"+machine.getName());
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
                        // todo: 未来更换成存入数据库中

                        //目的是当心跳断开时根据channel得知哪个服务器断线了
                        SocketServer.channelToName.put(ctx.channel(),machine.getName());//将channel和对应的客户端名字记录进Map

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
                    String status = content.get("type");
                    String machineObject = content.get("machineObject");
                    Machine machine = JSON.parseObject(machineObject, Machine.class);

                    if(status.equals("stop")){//客户端主动关闭了，要在数据库中登记
                        String clientName = machine.getName();
                        //把这个断线的客户端从记录中删除
                        SocketServer.nameToChannel.remove(clientName);
                        SocketServer.channelToName.remove(ctx.channel());
                        // todo: 修改数据库，未来可以用主键来获取machine，现在用list不方便
                        System.out.println(clientName+"已经关闭");
                        ctx.channel().close();

                    }
                    else{//status.equals("update")  客户端更新了自己的状态
                        System.out.println(machine.getName()+"更新了状态");
                        //SocketServer.machineList.add(machine);修改数据库中的内容
                        // todo: 修改数据库，未来可以用主键来获取machine，现在用list不方便

                    }
                }

                case client_heartBeat_telescreen:{//服务器收到心跳
                    //String clientName = SocketServer.channelToName.get(ctx.channel());
                    //System.out.println("收到"+clientName+"的心跳包");
                    break;
                }
            }
        }
        else{
            System.out.println("not instanceof");
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{//5秒未收到任何消息，将会断掉连接
        if(evt instanceof IdleStateEvent){
            //System.out.println("触发空闲事件");
            IdleStateEvent event = (IdleStateEvent)evt;
            //System.out.println("空闲事件类型："+event.state());
            if(event.state()==READER_IDLE){
                String clientName = SocketServer.channelToName.get(ctx.channel());//得到断线的clientName
                //把这个断线的客户端从记录中删除
                SocketServer.nameToChannel.remove(clientName);
                SocketServer.channelToName.remove(ctx.channel());
                // todo: 修改数据库，未来可以用主键来获取machine，现在用list不方便
                System.out.println(clientName+"超时未发送心跳包，断掉连接");

                //如果链接还管用，就向客户端报告，让它主动关闭服务器
                HashMap<String,String> responseContent = new HashMap<>();
                responseContent.put("type","delete");
                ctx.channel().writeAndFlush(Util.creatMessageString(MessageType.telescreen_changeClient_client,
                        responseContent));
                ChannelFuture future = ctx.channel().writeAndFlush("\r\n");//根据\r\n进行换行
                future.channel().close();
            }
        }

    }
}
