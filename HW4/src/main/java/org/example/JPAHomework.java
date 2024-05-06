package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.UUID;

public class JPAHomework {

    /**
     Перенести структуру дз третьего урока на JPA:
     1. Описать сущности Student и Group
     2. Написать запросы: Find, Persist, Remove
     3. ... поупражняться с разными запросами ...
     */


    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure();
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "root", "root");
             Statement statement = connection.createStatement();
             SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()){
            fillTable(session);
            setStudent(session);
            findAllStudents(statement);
            System.out.println();
            findStudentsByGroup(statement, "manager");
            System.out.println();
            removePersonByName(statement, connection, "Akakiy");
            System.out.println();
            System.out.println("=================== next HW4 ========================");
            getGroupByID(session, 2);
            addStudent(session);
            findAllStudents(statement);
            getStudentByName(session, "Billiboba");
            removeStudentByName(session, "Valentina");
            findAllStudents(statement);
        } catch (SQLException e) {
            System.err.println("Не удалось подключиться к БД: " + e.getMessage());
        }
    }

    /*
    Homework 4 start
     */

    static void fillTable (Session session) {
        Groups group = new Groups();
        Groups group2 = new Groups();
        group.setId(1L);
        group2.setId(2L);
        group.setGroup_name("developer");
        group2.setGroup_name("manager");

        Transaction tx = session.beginTransaction();
        session.persist(group);
        session.persist(group2);

        Student student = new Student();
        Student student2 = new Student();
        Student student3 = new Student();
        Student student4 = new Student();
        Student student5 = new Student();
        student.setId(UUID.randomUUID());
        student2.setId(UUID.randomUUID());
        student3.setId(UUID.randomUUID());
        student4.setId(UUID.randomUUID());
        student5.setId(UUID.randomUUID());
        student.setFirst_name("Anatoliy");
        student.setSecond_name("Ivanov");
        student2.setFirst_name("Akakiy");
        student2.setSecond_name("Petrov");
        student3.setFirst_name("Kristina");
        student3.setSecond_name("Sidorova");
        student4.setFirst_name("Valentina");
        student4.setSecond_name("Alekseeva");
        student5.setFirst_name("Prokofiy");
        student5.setSecond_name("Glebov");
        student.setGroups(group);
        student2.setGroups(group);
        student3.setGroups(group2);
        student4.setGroups(group);
        student5.setGroups(group2);

        session.persist(student);
        session.persist(student2);
        session.persist(student3);
        session.persist(student4);
        session.persist(student5);
        tx.commit();

    }

    static Groups getGroupByID (Session session, int id) {
        Groups group = session.find(Groups.class, id);
        System.out.println(group);
        return group;
    }

    static Student getStudentByName (Session session, String name) {
        Student student = session.createQuery("select s from Student s where first_name in ('" + name + "')", Student.class).getSingleResult();
        System.out.println(student);
        return student;
    }

    static void addStudent (Session session) {
        Student student = new Student();
        student.setId(UUID.randomUUID());
        student.setFirst_name("Billiboba");
        student.setSecond_name("Borisovich");
        student.setGroups(getGroupByID(session, 2));
        Transaction tx = session.beginTransaction();
        session.persist(student);
        tx.commit();
        System.out.println("New student added");
    }

    static void removeStudentByName (Session session, String name) {
        Transaction tx = session.beginTransaction();
        Student student = getStudentByName(session, name);
        session.remove(student);
        tx.commit();
        System.out.println(student + " успешно удален из таблицы");
    }

    static void setStudent(Session session) {
        Student student = getStudentByName(session, "Anatoliy");
        student.setFirst_name("New name");
        Transaction tx = session.beginTransaction();
        session.merge(student); // UPDATE
        tx.commit();
    }

    /*
    Homework 4 end
     */

    static void findAllStudents(Statement statement) throws SQLException{
        try {
            ResultSet resultSet = statement.executeQuery("""
                select s.id, s.first_name, s.second_name, Groups.group_name
                from Student as s
                inner join Groups
                on Groups.id = s.group_id
                """);

            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                String first_name = resultSet.getString("first_name");
                String second_name = resultSet.getString("second_name");
                String group_name = resultSet.getString("group_name");
                System.out.println("Строка: " + String.format("%s, %s, %s, %s", id, first_name, second_name, group_name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void findStudentsByGroup (Statement statement, String group) throws SQLException{
        try {
            ResultSet resultSet = statement.executeQuery(String.format("""
                select s.id, s.first_name, s.second_name, Groups.group_name
                from Student as s
                inner join Groups
                on Groups.id = s.group_id
                where group_name = '%s'
                """, group));

            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                String first_name = resultSet.getString("first_name");
                String second_name = resultSet.getString("second_name");
                String group_name = resultSet.getString("group_name");
                System.out.println("Строка: " + String.format("%s, %s, %s, %s", id, first_name, second_name, group_name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void removePersonByName(Statement statement, Connection connection, String name) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from Student where first_name = ?1")) {
            preparedStatement.setString(1, name);
            int deletedRowsCount = preparedStatement.executeUpdate();
            System.out.println("Удалено строк: " + deletedRowsCount);
        }
        System.out.println("Записи после удаления: ");
        findAllStudents(statement);
    }
}
