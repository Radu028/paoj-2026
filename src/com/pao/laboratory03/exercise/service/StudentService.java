package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.*;

public class StudentService {
    private static StudentService instance;
    private List<Student> students = new ArrayList<>();

    private StudentService() {}

    public static StudentService getInstance() {
        if (instance == null) instance = new StudentService();
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students)
            if (s.getName().equals(name))
                throw new RuntimeException("Studentul '" + name + "' exista deja");
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student s : students)
            if (s.getName().equals(name)) return s;
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost gasit");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        findByName(studentName).addGrade(subject, grade);
    }

    public void printAllStudents() {
        int i = 1;
        for (Student s : students) {
            System.out.println(i++ + ". " + s);
            for (Map.Entry<Subject, Double> e : s.getGrades().entrySet())
                System.out.println("   " + e.getKey().name() + " = " + e.getValue());
        }
    }

    public void printTopStudents() {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((a, b) -> Double.compare(b.getAverage(), a.getAverage()));
        System.out.println("=== Top studenti ===");
        int i = 1;
        for (Student s : sorted)
            System.out.printf("%d. %s - media: %.2f%n", i++, s.getName(), s.getAverage());
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> result = new HashMap<>();
        for (Subject subject : Subject.values()) {
            double sum = 0;
            int count = 0;
            for (Student s : students) {
                Double grade = s.getGrades().get(subject);
                if (grade != null) { sum += grade; count++; }
            }
            if (count > 0) result.put(subject, sum / count);
        }
        return result;
    }
}
