package org.example;

import java.sql.*;

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
            findStudentsByGroup(statement, "developer");
            System.out.println();
            removePersonById(statement, connection, 1);
        } catch (SQLException e) {
            System.err.println("Не удалось подключиться к БД: " + e.getMessage());
        }
    }

    static void createTable (Statement statement) throws SQLException{
        try {
            statement.execute("""
                create table Student(
                    id bigint,
                    first_name varchar(256),
                    second_name varchar(256),
                    group_name varchar(128)
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void fillTable (Statement statement) throws SQLException{
        try {
            int count = statement.executeUpdate("""
                insert into Student(id, first_name, second_name, group_name) values
                    (1, 'Anatoliy', 'Ivanov', 'developer'),
                    (2, 'Akakiy', 'Petrov', 'developer'),
                    (3, 'Kristina', 'Sidorova', 'manager'),
                    (4, 'Valentina', 'Alekseeva', 'developer'),
                    (5, 'Prokofiy', 'Glebov', 'manager')
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
                    set group_name = 'developer'
                    where id > 4
                """);
            System.out.println("Количество обновленных строк: " + count);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void findAllStudents(Statement statement) throws SQLException{
        try {
            ResultSet resultSet = statement.executeQuery("""
                select id, first_name, second_name, group_name
                from Student
                """);


            while (resultSet.next()) {
                int id = resultSet.getInt("id");
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
                select id, first_name, second_name, group_name
                from Student
                where group_name = '%s'
                """, group));


            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String first_name = resultSet.getString("first_name");
                String second_name = resultSet.getString("second_name");
                String group_name = resultSet.getString("group_name");
                System.out.println("Строка: " + String.format("%s, %s, %s, %s", id, first_name, second_name, group_name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void removePersonById(Statement statement, Connection connection, int idParameter) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from Student where id = ?1")) {
            preparedStatement.setLong(1, idParameter);
            int deletedRowsCount = preparedStatement.executeUpdate();
            System.out.println("Удалено строк: " + deletedRowsCount);
        }
        System.out.println("Записи после удаления: ");
        findAllStudents(statement);
    }
}
