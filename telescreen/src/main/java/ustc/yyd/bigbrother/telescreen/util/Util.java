package ustc.yyd.bigbrother.telescreen.util;

import com.alibaba.fastjson.JSONObject;
import ustc.yyd.bigbrother.data.Message;
import ustc.yyd.bigbrother.data.MessageType;

import java.util.HashMap;

public class Util {
    public static String creatMessageString(MessageType type, HashMap<String,String> content){
        Message message = new Message();
        message.setContent(content);
        message.setType(type);
        return JSONObject.toJSONString(message);
    }
}
