package ustc.yyd.bigbrother.webserver.controller;

import org.springframework.web.bind.annotation.*;
import ustc.yyd.bigbrother.data.Color;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.webserver.data.DataBase;
import ustc.yyd.bigbrother.webserver.socket.SocketClient;

/*
* 用于处理web请求的controller
* */
@RestController
public class Controller {
    @RequestMapping("/stopOneClient")
    public String stopOneClient(String clientName){
        System.out.println("停止"+clientName);
        SocketClient.stopOneClient(clientName);
        //todo 修改数据库
        return "success";
    }


    @PostMapping("/changeClientAuto")
    public String changeClientAuto(String clientName){
        //todo：从数据库里根据clientName来获取machine
        Machine machine = DataBase.machineMap.get(clientName);
        if(machine==null)
            return "fail";
        machine.setAutoChange(!machine.isAutoChange());
        SocketClient.changeOneClient(clientName, machine);
        machine.setAutoChange(!machine.isAutoChange());
        return "success";
    }

    @PostMapping("/changeClientColor")
    public String changeClientColor(String clientName, String red,String green,String blue){//目前不知道用什么参数好，具体看前端实现
        //todo：从数据库里根据clientName来获取machine
        Machine machine = DataBase.machineMap.get(clientName);//模拟从数据库中取出
        if(machine==null)
            return "fail";
        Color newColor;
        try {
            newColor=new Color(Integer.parseInt(red),Integer.parseInt(green),Integer.parseInt(blue));
        }catch (Exception e)
        {
            return "fail";
        }
        Color oldColor=machine.getColor();
        machine.setColor(newColor);
        SocketClient.changeOneClient(clientName, machine);
        machine.setColor(oldColor); //变回原来的颜色，因为这不是客户端上报的
        return "success";
    }
}
