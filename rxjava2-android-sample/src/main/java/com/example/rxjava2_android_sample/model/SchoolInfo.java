package com.example.rxjava2_android_sample.model;

/**
 * Created by mengtao1 on 2017/12/20.
 */

public class SchoolInfo {
    private static SchoolClass[] mSchoolClasses = null;

    private static SchoolClass[] getSchoolClassData() {
        SchoolClass[] mSchoolClasses=new SchoolClass[2];
        Student[] student=new Student[5];
        for(int i=0;i<5;i++){
            Student s=new Student("No.1 school", "stu1"+i,"17");
            student[i]=s;
        }
        mSchoolClasses[0]=new SchoolClass(student);

        Student[] student2=new Student[5];
        for(int i=0;i<5;i++){
            Student s=new Student("No.2 school", "stu2"+i,"18");
            student2[i]=s;
        }
        mSchoolClasses[1]=new SchoolClass(student2);
        return mSchoolClasses;
    }

    public static class SchoolClass{
        Student[] stud;
        public SchoolClass(Student[] s){
            this.stud=s;
        }
        public Student[] getStudents(){
            return  stud;
        }
    }

    public static class Student {
        private String schoolName;
        private String name;
        private String age;

        private Student(String schoolName, String name, String age) {
            this.schoolName = schoolName;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString(){
            return "\"Student: "+schoolName+" "+name+" "+age+"\"";
        }
    }

    public static SchoolClass[] getData(){
        if(null == mSchoolClasses)mSchoolClasses = getSchoolClassData();
        return  mSchoolClasses;
    }
}
