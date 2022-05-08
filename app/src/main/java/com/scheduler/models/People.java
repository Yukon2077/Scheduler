package com.scheduler.models;

import java.util.Objects;

public class People {
    String name;
    String number;

    public People(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        People people = (People) o;
        return getName().equals(people.getName()) && getNumber().equals(people.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
