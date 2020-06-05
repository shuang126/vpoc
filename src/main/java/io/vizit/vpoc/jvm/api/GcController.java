package io.vizit.vpoc.jvm.api;

import io.vizit.vpoc.jvm.GcSupervisor;
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
    private final Heap heap;
    private final GcSupervisor gcSupervisor;
    private final SimpMessageSendingOperations messagingTemplate;

    public GcController(SimpMessageSendingOperations messagingTemplate, Heap heap, GcSupervisor gcSupervisor) {
        this.messagingTemplate = messagingTemplate;
        this.heap = heap;
        this.gcSupervisor = gcSupervisor;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public @ResponseBody
    List<ObjectBO> newObject(@RequestBody NewRequest request) {
        List<ObjectBO> objects = new ArrayList<>();
        gcSupervisor.setDelay(request.getDelay());
        if (request.isReset()) {
            heap.clear();
        }
        gcSupervisor.setDebug(request.isDebug());

        for (int i = 0; i < request.getCount(); i++) {
            int size = request.getSize();
            if (request.getRandomSizeMax() > 0) {
                size = ThreadLocalRandom.current().nextInt(1, request.getRandomSizeMax());
            }
            ObjectBO objectBO = heap.allocate(size);
            objects.add(objectBO);
        }
        return objects;
    }

    @RequestMapping(value = "/debug/go", method = RequestMethod.POST)
    public @ResponseBody
    Boolean go() {
        return gcSupervisor.go();
    }

    @RequestMapping(value = "/debug/step", method = RequestMethod.POST)
    public @ResponseBody
    Boolean step() {
        return gcSupervisor.step();
    }

    @RequestMapping(value = "/debug/pause", method = RequestMethod.POST)
    public @ResponseBody
    Boolean pause() {
        gcSupervisor.setDebug(true);
        return true;
    }

}
