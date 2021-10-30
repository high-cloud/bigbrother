package ustc.yyd.bigbrother.machine;

import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.machine.net.SocketClient;
import ustc.yyd.bigbrother.machine.util.Util;

import java.util.Scanner;


public class MachineApplication {
    //private static String telescreenIp;  未来可以从命令行中获取ip和端口
    //private static int telesreenPort;

    //要设为类变量，保证channel能够修改这些变量，无需线程保护，只有一个线程能对他修改
    public static Machine machine = new Machine();//机器信息
    public static boolean run = true;//命令行循环变量
    public static SocketClient client = null;//netty线程池

    public static void main(String[] args) throws InterruptedException {
        machine.setAutoChange(false); // 暂时不开启自动换色功能
        machine.setColor(Util.randomColorRGB());
        machine.setName(Util.randomName(3));
        machine.setOnline(true);
        //SocketClient client = new SocketClient(telesreenPort,telescreenIp);
        client = new SocketClient(1984,"localhost");

        Scanner input=new Scanner(System.in);
        while (run){
            System.out.println("请输入指令：");
            String str = input.next();
            if(!run){
                //在等待指令阻塞时客户端已经停止了，这时候直接停掉进程
                break;
            }
            switch (str){
                case "init":
                {
                    client.init(machine);
                    break;
                }
            }
            Thread.sleep(5000);
        }
        System.out.println("客户端已停止");

    }


}
