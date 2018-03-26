package com.young.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="student")
@XmlAccessorType(XmlAccessType.FIELD)
public class Student2{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String name;
    
    @XmlAttribute
    private String depart;
    
    private String country;
    
    @XmlElementWrapper(name="courses")
    @XmlElement(name="course")
    //private List<String> courses; 
    private List<Course> courses;


    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

  
    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

}
