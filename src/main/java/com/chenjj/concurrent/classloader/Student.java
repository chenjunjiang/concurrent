package com.chenjj.concurrent.classloader;

public class Student {
    private Student student;

    public void setStudent(Object object) {
        this.student = (Student) object;
    }
}
