package ustc.yyd.bigbrother.telescreen;

import ustc.yyd.bigbrother.telescreen.net.SocketServer;

/*
    socket服务器启动方法
 */
public class TelescreenApplication {

    public static void main(String[] args) {//监听1984端口
        SocketServer.run(1984);
    }
}
