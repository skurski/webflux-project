package demo.webflux.server.controller;

import demo.webflux.server.entity.Motorcycle;
import demo.webflux.server.entity.Specification;
import demo.webflux.server.repository.MotorcycleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class MotorcycleController {

    private static final Logger logger = LoggerFactory.getLogger(MotorcycleController.class);

    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    private MotorcycleDao motorcycleDao;

    @Autowired
    public MotorcycleController(MotorcycleDao motorcycleDao) {
        this.motorcycleDao = motorcycleDao;
    }

    @GetMapping(value = "/motorcycle/{id}", produces = "application/json")
    public Motorcycle getMotorcycle(@PathVariable String id) throws InterruptedException {
        Thread.sleep(2000L);

        logger.debug("Request for motorcycle data, id: " + id);
        return motorcycleDao.get(id);
    }

    @GetMapping(value = "/motorcycle/{id}/specification", produces = "application/json")
    public Specification getSpecification(@PathVariable String id) throws InterruptedException {
        Thread.sleep(2000L);

        logger.debug("Request for specification for motorcycle id: " + id);
        return motorcycleDao.get(id).getSpecs();
    }

    @GetMapping(value = "/motorcycles/stream", produces = "application/json+stream")
    public SseEmitter motorcycleSseStream() {
        SseEmitter emitter = new SseEmitter();
        nonBlockingService.execute(() -> {
            try {
                for (Motorcycle moto: motorcycleDao.getAll()) {
                    Thread.sleep(2000L);
                    emitter.send(moto);
                }
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}
