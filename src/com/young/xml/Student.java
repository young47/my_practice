package com.young.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="student")
public class Student /*implements Serializable*/ {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String name;
    
    
    private String depart;
    
    private String country;
    
    
    private List<String> courses; //这里的名字不能与Wrapper中的名字相同，即不能为courses

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

    @XmlAttribute
    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public List<String> getCourses() {
        return courses;
    }

    @XmlElementWrapper(name="courses")
    @XmlElement(name="course")
    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

}
