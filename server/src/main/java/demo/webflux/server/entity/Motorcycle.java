package demo.webflux.server.entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Motorcycle {

    private String id;

    private String make;

    private String model;

    private Specification specs;
}
