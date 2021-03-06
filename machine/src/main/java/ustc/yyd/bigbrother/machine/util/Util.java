package ustc.yyd.bigbrother.machine.util;

import com.alibaba.fastjson.JSONObject;
import ustc.yyd.bigbrother.data.Color;
import ustc.yyd.bigbrother.data.Message;
import ustc.yyd.bigbrother.data.MessageType;

import java.util.HashMap;
import java.util.Random;

public class Util {

    public static Color randomColorRGB()
    {
        Random rand=new Random();
        int red=rand.nextInt(256);
        int green=rand.nextInt(256);
        int blue = rand.nextInt(256);
        return new Color(red,green,blue);
    }

    public static String randomName(int length)
    {
        Random rand=new Random();
        StringBuffer sb=new StringBuffer();
        int caseint;
        int specificCharint;
        for(int i=0;i<length;++i)
        {
            caseint=rand.nextInt(3);
            switch (caseint){
                case 0:
                    // a-z
                    specificCharint=rand.nextInt(25)+65;
                    sb.append(String.valueOf((char)specificCharint));
                    break;
                case 1:
                    //A-Z
                    specificCharint=rand.nextInt(25)+97;
                    sb.append(String.valueOf((char)specificCharint));
                    break;
                case 2:
                    //0-9
                    sb.append(String.valueOf(rand.nextInt(10)));
                    break;

            }
        }

        return sb.toString();
    }

    public static String creatMessageString(MessageType type, HashMap<String,String> content){
        Message message = new Message();
        message.setContent(content);
        message.setType(type);
        return JSONObject.toJSONString(message);
    }
}
