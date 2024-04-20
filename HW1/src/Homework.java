import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Homework {

    /**
     * Используя классы Person и Department, реализовать методы ниже:
     */


    /**
     * Найти количество сотрудников, старше age лет с зарплатой больше, чем salary
     */
    public int countPersons(List<Person> persons, int age, double salary) {
        return persons.stream()
                .filter(person -> person.getAge() > age)
                .filter(person -> person.getSalary() > salary)
                .toList()
                .size();

//        throw new UnsupportedOperationException();
    }

    /**
     * Найти среднюю зарплату сотрудников, которые работают в департаменте dep
     */
    public OptionalDouble averageSalary(List<Person> persons, Department dep) {
        return OptionalDouble.of(persons.stream()
                .filter(person -> person.getDepartment().equals(dep))
                .collect(Collectors.averagingDouble(Person::getSalary)));

//        throw new UnsupportedOperationException();
    }

    /**
     * Сгруппировать сотрудников по департаментам
     */
    public Map<Department, List<Person>> groupByDepartment(List<Person> persons) {
        return persons.stream()
                .collect(Collectors.groupingBy(Person::getDepartment));

//        throw new UnsupportedOperationException();
    }

    /**
     * Найти максимальные зарплаты по отделам
     */
    public Map<Department, Double> maxSalaryByDepartment(List<Person> persons) {
        return persons.stream()
                .collect(Collectors.toMap(Person::getDepartment, Person::getSalary, Math::max));

//        throw new UnsupportedOperationException();
    }

    /**
     * ** Сгруппировать имена сотрудников по департаментам
     */
    public Map<Department, List<String>> groupPersonNamesByDepartment(List<Person> persons) {
        return persons.stream()
                .collect(Collectors.groupingBy(Person::getDepartment,
                        mapping(Person::getName, toList())));

//        throw new UnsupportedOperationException();
    }

    /**
     * ** Найти сотрудников с минимальными зарплатами в своем отделе
     */
    public List<Person> minSalaryPersons(List<Person> persons) {
        List<Person> res = new ArrayList<>();
        /*
        Вариант 1
         */
//        for (List<Person> value : groupByDepartment(persons).values()) {
//            res.add(value.stream()
//                    .min(Comparator.comparingDouble(Person::getSalary))
//                    .get());
//        }

        /*
        Вариант 2
         */
        groupByDepartment(persons).values().forEach(value ->
                res.add(value.stream()
                        .min(Comparator.comparingDouble(Person::getSalary))
                        .get()));

        /*
        Вариант 3
        Подозреваю, что существует еще более изящный вариант решения, но я его так и не сочинил...
         */
//        persons.stream()
//                .collect(groupingBy(Person::getDepartment)
//                        .
//                .min(Comparator.comparingDouble(Person::getSalary))
//                .get();

        return res;

        // В каждом департаменте ищем сотрудника с минимальной зарплатой.
        // Всех таких сотрудников собираем в список и возвращаем из метода.
//        throw new UnsupportedOperationException();
    }

}