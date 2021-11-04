package ustc.yyd.bigbrother.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import ustc.yyd.bigbrother.webserver.socket.SocketClient;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class WebserverApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebserverApplication.class, args);
        SocketClient client = new SocketClient(1985,"localhost");
    }
}
