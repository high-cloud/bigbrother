package ustc.yyd.bigbrother.webserver.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.webserver.data.DataBase;
import ustc.yyd.bigbrother.webserver.socket.SocketClient;

import java.util.ArrayList;

/*
 * 返回页面的controller
 * */
@Controller
public class WebController {
    @GetMapping("/tables")
    public String tables() {
        System.out.println(DataBase.machineMap.size());

        return "tables";
    }

    @GetMapping("/")
    public String index() {
        System.out.println(DataBase.machineMap.size());

        return "tables";
    }

    @GetMapping("/clients")
    @ResponseBody
    public String getClients(){
        return JSON.toJSONString(DataBase.machineMap.values());
    }


}
