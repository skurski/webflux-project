package demo.webflux.server.repository;

import demo.webflux.server.entity.Motorcycle;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MotorcycleDao {

    private Map<String, Motorcycle> motorcycleMap = new HashMap<>();

    public void put(Map<String, Motorcycle> motorcycles) {
        this.motorcycleMap = motorcycles;
    }

    public Motorcycle get(String id) {
        return motorcycleMap.get(id);
    }

    public List<Motorcycle> getAll() {
        return new ArrayList<>(motorcycleMap.values());
    }
}

