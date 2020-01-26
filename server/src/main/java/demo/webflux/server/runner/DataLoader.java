package demo.webflux.server.runner;

import demo.webflux.server.entity.Motorcycle;
import demo.webflux.server.entity.Specification;
import demo.webflux.server.repository.MotorcycleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Map<String, Motorcycle> motorcycleMap = new HashMap<>();

    static {
        motorcycleMap.put("1", new Motorcycle("1", "Suzuki", "Bandit 650",
                new Specification(2000, "red", 6500)));
        motorcycleMap.put("2", new Motorcycle("2", "Suzuki", "V-Strom 650",
                new Specification(2005, "black", 15500)));
        motorcycleMap.put("3", new Motorcycle("3", "Honda", "Varadero 1000",
                new Specification(2002, "blue", 7200)));
        motorcycleMap.put("4", new Motorcycle("4", "Kawasaki", "Vulcan 900",
                new Specification(1997, "green", 12000)));
    }

    @Autowired
    private MotorcycleDao motorcycleDao;

    @Override
    public void run(String... args) throws Exception {
        motorcycleDao.put(motorcycleMap);
    }
}

