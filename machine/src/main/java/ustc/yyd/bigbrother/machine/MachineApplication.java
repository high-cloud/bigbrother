package ustc.yyd.bigbrother.machine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.machine.util.util;

@SpringBootApplication
@Component
public class MachineApplication implements CommandLineRunner {
    @Value("127.0.0.1")
    private String telescreenIp;
    @Value("1984")
    private int telesreenPort;

    private Machine machine;
    public static void main(String[] args) {
        SpringApplication.run(MachineApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        machine.setAutoChange(false); // 暂时不开启自动换色功能
        machine.setColor(util.randomColorRGB());
        machine.setName(util.randomName(10));


        // todo: connect socket server
    }
}
