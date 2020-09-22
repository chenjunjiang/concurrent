package com.chenjj.concurrent.classloader;

public class Student {
    private Student student;

    public void setStudent(Object object) {
        System.out.println("当前对象的classloader is: " + this.getClass().getClassLoader());
        System.out.println("object's classloader is: " + object.getClass().getClassLoader());
        this.student = (Student) object;
    }
}
