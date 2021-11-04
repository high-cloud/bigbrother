package ustc.yyd.bigbrother.webserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.webserver.data.DataBase;

import java.util.ArrayList;

/*
 * 返回页面的controller
 * */
@Controller
public class WebController {
    @GetMapping("/")
    public String index(ModelMap map) {
        System.out.println(DataBase.machineMap.size());
        ArrayList<Machine> machineList = new ArrayList<>(DataBase.machineMap.values());
        map.put("machineList",machineList);

        return "index";
    }
}
