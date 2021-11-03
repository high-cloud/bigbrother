package ustc.yyd.bigbrother.webserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ustc.yyd.bigbrother.webserver.socket.SocketClient;

@RestController
public class Controller {
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        //SocketClient.change(); 通过这种方式来向socket服务器下达指令
        return String.format("Hello %s!", name);
    }
}
