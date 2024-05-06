package org.example;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Groups {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "group_name")
    private String group_name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private List<Student> students;

    public Groups() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", group_name='" + group_name + '\'' +
                '}';
    }
}
