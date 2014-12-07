package com.bus.services.util;

import org.springframework.data.domain.Sort;

import java.io.Serializable;

public class Order implements Serializable {

    private Sort.Direction direction;
    private String property;

    public Order() {
    }

    public Order(Sort.Order sortOrder) {
        direction = sortOrder.getDirection();
        property = sortOrder.getProperty();
    }

    public String getProperty() { return property; }
    public Order setProperty(String value) { this.property = value; return this; }

    public Sort.Direction getDirection() { return direction; }
    public Order setDirection(Sort.Direction value) { this.direction = value; return this; }
}
