package org.example;

import java.sql.*;
import java.util.UUID;

public class Homework {

    /**
     * Повторить все, что было на семниаре на таблице Student с полями
     * 1. id - bigint
     * 2. first_name - varchar(256)
     * 3. second_name - varchar(256)
     * 4. group - varchar(128)
     *
     * Написать запросы:
     * 1. Создать таблицу
     * 2. Наполнить таблицу данными (insert)
     * 3. Поиск всех студентов
     * 4. Поиск всех студентов по имени группы
     *
     * Доп. задания:
     * 1. ** Создать таблицу group(id, name); в таблице student сделать внешний ключ на group
     * 2. ** Все идентификаторы превратить в UUID
     *
     * Замечание: можно использовать ЛЮБУЮ базу данных: h2, postgres, mysql, ...
     */


    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "root", "root");
             Statement statement = connection.createStatement()){
            createTable(statement);
            fillTable(statement);
            setTable(statement);
            findAllStudents(statement);
            System.out.println();
            findStudentsByGroup(statement, "manager");
            System.out.println();
            removePersonByName(statement, connection, "Akakiy");
        } catch (SQLException e) {
            System.err.println("Не удалось подключиться к БД: " + e.getMessage());
        }
    }

    static void createTable (Statement statement) throws SQLException{
        try {
            statement.execute("""
                create table Groups(
                    id integer not null auto_increment primary key,
                    group_name varchar(128)
                )
                """);

            statement.execute("""
                create table Student(
                    id uuid default random_uuid() primary key,
                    first_name varchar(256),
                    second_name varchar(256),
                    group_id integer not null references Groups(id)
                )
                """);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

static void fillTable (Statement statement) throws SQLException{
    try {
        statement.executeUpdate("""
                insert into Groups(group_name) values
                    ('developer'),
                    ('manager')
                """);

        int count = statement.executeUpdate("""
                insert into Student(first_name, second_name, group_id) values
                    ('Anatoliy', 'Ivanov', 1),
                    ('Akakiy', 'Petrov', 1),
                    ('Kristina', 'Sidorova', 2),
                    ('Valentina', 'Alekseeva', 1),
                    ('Prokofiy', 'Glebov', 2)
                """);
            System.out.println("Колличество вставленных строк: " + count);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void setTable (Statement statement) throws SQLException{
        try {
            int count = statement.executeUpdate("""
                update Student
                    set first_name = 'New_Name'
                    where first_name = 'Anatoliy'
                """);
            System.out.println("Количество обновленных строк: " + count);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
