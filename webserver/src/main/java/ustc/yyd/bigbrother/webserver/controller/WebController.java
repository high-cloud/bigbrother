package ustc.yyd.bigbrother.webserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ustc.yyd.bigbrother.data.Machine;

import java.util.ArrayList;
import java.util.List;

/*
* 返回页面的controller
* */
@Controller
public class WebController {
    @GetMapping("/index")
    public String index(ModelMap map) {

        map.put("text","text");

        return "index";
    }
}
