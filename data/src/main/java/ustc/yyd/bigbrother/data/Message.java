/*******************************************************************************
 * Copyright © 2021 YangYaodong
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 ******************************************************************************/

package ustc.yyd.bigbrother.data;

import java.util.HashMap;
/*
    Message key list:
    client_register_telescreen:客户端向服务器注册
        machineObject:客户端对象的JSON字符串

    telescreen_confirm_client:服务器向客户端确认注册
        result:①success（注册成功） ②fail（注册失败）

    client_heartBeat_telescreen：客户端向服务器发送心跳
        暂时不放数据

    telescreen_changeClient_client:
        type: ①delete（关闭这个服务器） ②setColor（修改颜色） ③setAutoChange（修改是否自动变色）
        autoChange：①true ②false（只有type是setAutoChange时才有）
        colorObject：Color对象的JSON字符串（只有type是setColor时才有）

    client_report_telescreen：
        type:①stop（客户端关闭） ②update（客户端更新）
        machineObject:客户端对象

*/

public class Message {
    private MessageType type;
    private HashMap<String,String> content;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public HashMap<String, String> getContent() {
        return content;
    }

    public void setContent(HashMap<String, String> content) {
        this.content = content;
    }
}
