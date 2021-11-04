package ustc.yyd.bigbrother.telescreen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.telescreen.net.SocketServer;

import java.util.ArrayList;

/*
* 返回页面的controller
* */
@Controller
public class WebController {
    @GetMapping("/index")
    public String index(ModelMap map) {
        ArrayList<Machine> machineList = new ArrayList<>(SocketServer.machineMap.values());
        map.put("machineList",machineList);

        return "index";
    }
}
