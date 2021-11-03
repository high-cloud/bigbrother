package ustc.yyd.bigbrother.webserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ustc.yyd.bigbrother.data.Color;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.webserver.socket.SocketClient;

/*
* 用于处理web请求的controller
* */
@RestController
public class Controller {
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        //SocketClient.change(); 通过这种方式来向socket服务器下达指令
        return String.format("Hello %s!", name);
    }

    @RequestMapping("/stopOneClient")
    public String stopOneClient(String clientName){
        System.out.println("停止"+clientName);
        SocketClient.stopOneClient(clientName);
        return "success";
    }

    @RequestMapping("/changeClientAuto")
    public String changeClientAuto(String clientName, String autoColor){
        //todo：从数据库里根据clientName来获取machine
        Machine machine = new Machine();//模拟从数据库中取出
        machine.setAutoChange("true".equals(autoColor));
        SocketClient.changeOneClient(clientName, machine);
        return "success";
    }

    @RequestMapping("/changeClientColor")
    public String changeClientColor(String clientName, Color color){//目前不知道用什么参数好，具体看前端实现
        //todo：从数据库里根据clientName来获取machine
        Machine machine = new Machine();//模拟从数据库中取出
        machine.setColor(color);
        SocketClient.changeOneClient(clientName, machine);
        return "success";
    }
}
