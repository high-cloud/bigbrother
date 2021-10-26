package ustc.yyd.bigbrother.machine;

import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.machine.net.SocketClient;
import ustc.yyd.bigbrother.machine.util.util;


public class MachineApplication {
    private static String telescreenIp;
    private static int telesreenPort;

    private static Machine machine = new Machine();
    public static void main(String[] args) {
        machine.setAutoChange(false); // 暂时不开启自动换色功能
        machine.setColor(util.randomColorRGB());
        machine.setName(util.randomName(3));
        SocketClient client = new SocketClient(machine,1984,"localhost");
        client.run();
    }


}
