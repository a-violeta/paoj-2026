package com.pao.laboratory02.exercise2;

import java.util.Objects;

/**
 * TODO: Adaugă equals(Object o) și hashCode() — doi studenți sunt egali dacă au același id.
 * Model: vezi equalshashcode/Book.java
 */
public class Student {
    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;              // același obiect
        if (o == null || getClass() != o.getClass()) return false; // alt tip

        Student student = (Student) o;
        return id == student.id;                 // egalitate bazată DOAR pe id
    }

    public int hashCode() {
        return Objects.hash(id);                 // hash bazat DOAR pe id
    }
}
