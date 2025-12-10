package com.mitocode.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "dishes")
public class Dish {

    @Id
    private String id;

    private String name;
    private Double price;
    private Boolean status;

}
