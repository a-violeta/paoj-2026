package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.*;

public class StudentService {

    private static StudentService instance;

    private List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {

        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul exista deja.");
            }
        }

        students.add(new Student(name, age));
    }

    public Student findByName(String name) {

        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }

        throw new StudentNotFoundException("Studentul nu a fost gasit.");
    }

    public void addGrade(String studentName, Subject subject, double grade) {

        Student student = findByName(studentName);

        student.addGrade(subject, grade);
    }

    public void printAllStudents() {

        for (Student s : students) {
            System.out.println(s);
            System.out.println("Note: " + s.getGrades());
        }
    }

    public void printTopStudents() {

        students.stream()
                .sorted((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()))
                .forEach(System.out::println);
    }

    public Map<Subject, Double> getAveragePerSubject() {

        Map<Subject, List<Double>> temp = new HashMap<>();

        for (Student s : students) {

            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {

                temp.putIfAbsent(entry.getKey(), new ArrayList<>());
                temp.get(entry.getKey()).add(entry.getValue());
            }
        }

        Map<Subject, Double> result = new HashMap<>();

        for (Map.Entry<Subject, List<Double>> entry : temp.entrySet()) {

            double sum = 0;

            for (double g : entry.getValue()) {
                sum += g;
            }

            result.put(entry.getKey(), sum / entry.getValue().size());
        }

        return result;
    }
}