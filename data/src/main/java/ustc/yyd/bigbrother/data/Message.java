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
    client_register_telescreen:客户端向socket服务器注册
        machineObject:客户端对象的JSON字符串

    telescreen_confirm_client:socket服务器向客户端确认注册
        result:①success（注册成功） ②fail（注册失败）

    client_heartBeat_telescreen：客户端向服务器发送心跳
        暂时不放数据

    telescreen_changeClient_client:socket服务器通知客户端更改状态
        type: ①stop（关闭这个客户端） ②update（修改这个客户端的状态）
        machineObject:客户端对象的JSON字符串（只有type是update才有）

    client_report_telescreen：客户端向socket服务器通知状态改变
        type:①stop（客户端关闭） ②update（客户端更新）
        machineObject:客户端对象的JSON处字符串

    webserver_register_telescreen:web服务器向socket服务器登记
        name:"@@" (@@这个名字普通的客户端无法生成，所以这个名字可以专属于webServer)

    telescreen_newClient_webserver:socket服务器告知web服务器有新客户端
        name:新增machine的名字
        machineObject:客户端对象的JSON字符串

    telescreen_clientChange_webserver:socket服务器通知web服务器客户端状态改变
        name:状态改变的machine的名字
        type:①stop（客户端关闭） ②update（客户端更新） （状态改变的种类）
        machineObject:客户端对象的JSON字符串（如果是stop，则为null）

    webserver_changeClient_telescreen：web服务器告知socket服务器更改客户端状态
        name:客户端名字
        type:①stop（停止这个客户端运行） ②update（改变这个客户端状态）
        machineObject:客户端对象的JSON字符串（只有type是update才有）

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
