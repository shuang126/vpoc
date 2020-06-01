package io.vizit.vpoc.api.jvm;
import io.vizit.vpoc.mq.KafkaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
public class JvmController {
    private final KafkaClient kafkaClient;

    public JvmController(KafkaClient kafkaClient) {
        this.kafkaClient = kafkaClient;
    }

    @RequestMapping("/jvm")
    public String index() {
        kafkaClient.send("jvm", "init heap");
        return "Greetings from JVM PoC!";
    }
}
