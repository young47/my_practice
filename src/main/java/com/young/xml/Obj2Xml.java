package com.young.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Obj2Xml {
    
    public static final File xmlFile = new File("student.xml");
    public static final File xmlFile2 = new File("student2.xml");
    

    /**
     * @param args
     * @throws JAXBException 
     */
    public static void main(String[] args) throws JAXBException {

        Student2 stu = construct(new Student2());
        
        getMarshaller(Student2.class).marshal(stu, xmlFile2);
        
        
    }

    private static Student2 construct(Student2 student) {
        
        student.setName("Young2");
        
        student.setCountry("China");
        
        student.setDepart("ADM");
        
        List<String> list = Arrays.asList("Math","English","Computer","Society");
        
        //stu.setStuCourses(list);
       List<Course> courses = new ArrayList<Course>(); 
        for (String string : list) {
            Course course = new Course();
            course.setName(string);
            courses.add(course);
        }
        student.setCourses(courses);
        
        return student;
    }

    private static Marshaller getMarshaller(Class clas) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(clas);
        
        Marshaller marshaller = ctx.createMarshaller();
        
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        
        return marshaller ;
    }


}
