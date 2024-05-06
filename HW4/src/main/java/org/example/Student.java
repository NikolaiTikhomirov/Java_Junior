package org.example;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Student")
public class Student {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "second_name")
    private String second_name;

//    @Column(name = "group_id")
//    private Long group_id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Groups groups;

    public Student () {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

//    public Long getGroup_id() {
//        return group_id;
//    }
//
//    public void setGroup_id(Long group_id) {
//        this.group_id = group_id;
//    }

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", second_name='" + second_name + '\'' +
                ", group_id=" + groups +
                '}';
    }
}
