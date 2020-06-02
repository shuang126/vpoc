package io.vizit.vpoc.jvm.api;

import com.google.gson.Gson;
import io.vizit.vpoc.jvm.model.Heap;
import io.vizit.vpoc.jvm.model.ObjectBO;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(value = "/jvm/gc")
public class GcController {
    private static final String TOPIC_GC = "/topic/gc/new";
    private Heap heap = Heap.getInstance();
    private final SimpMessageSendingOperations messagingTemplate;

    public GcController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    List<ObjectBO> newObject(@RequestBody NewRequest request) {
        List<ObjectBO> objects = new ArrayList<>();
        for (int i = 0; i < request.getCount(); i++) {
            int size = request.getSize();
            if (request.getRandomSizeMax() > 0) {
                size = ThreadLocalRandom.current().nextInt(1, request.getRandomSizeMax());
            }
            ObjectBO objectBO = heap.allocate(size);
            objects.add(objectBO);
        }
        messagingTemplate.convertAndSend(TOPIC_GC, objects);
        return objects;
    }
}
