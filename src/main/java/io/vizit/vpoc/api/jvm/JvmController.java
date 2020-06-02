package io.vizit.vpoc.api.jvm;
import io.vizit.vpoc.mq.KafkaClient;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;
class Greeting {

    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
class HelloMessage {

    private String name;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
@RestController
public class JvmController {
    public static final String TOPIC_GREETINGS = "/topic/jvm";
    private final KafkaClient kafkaClient;
    private final SimpMessageSendingOperations messagingTemplate;

    public JvmController(KafkaClient kafkaClient, SimpMessageSendingOperations messagingTemplate) {
        this.kafkaClient = kafkaClient;
        this.messagingTemplate = messagingTemplate;
    }

    @RequestMapping("/jvm")
    public String index() {
//        kafkaClient.send("jvm", "init heap");
        Greeting greeting = new Greeting("Hello, init heap !");
        messagingTemplate.convertAndSend(TOPIC_GREETINGS, greeting);
        return "Greetings from JVM PoC!";
    }

    @MessageMapping("/hello")
    @SendTo(TOPIC_GREETINGS)
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
