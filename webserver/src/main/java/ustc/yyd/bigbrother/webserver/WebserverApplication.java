package ustc.yyd.bigbrother.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ustc.yyd.bigbrother.data.Machine;
import ustc.yyd.bigbrother.webserver.socket.SocketClient;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
//@RestController
public class WebserverApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebserverApplication.class, args);
        SocketClient client = new SocketClient(1984,"localhost");
    }
}
