package demo.webflux.server.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Specification {

    private int year;

    private String colour;

    private double price;
}
