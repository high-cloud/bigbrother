package ustc.yyd.bigbrother.telescreen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import ustc.yyd.bigbrother.telescreen.net.SocketServer;

/*
    socket服务器启动方法
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class TelescreenApplication {

    public static void main(String[] args) {//监听1985端口
        SpringApplication.run(TelescreenApplication.class, args);
        SocketServer.run(1985);
    }
}
