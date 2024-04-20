import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Homework homework = new Homework();
        Department dev = new Department("Development");
        Department sales = new Department("Sales department");
        Department top = new Department("Top Managers");

        Person person1 = new Person("Kasimir", 23, 100, dev);
        Person person2 = new Person("Anufriy", 32, 150, dev);
        Person person3 = new Person("Zahar", 27, 125, dev);
        Person person4 = new Person("Brus", 20, 200, sales);
        Person person5 = new Person("Jackie", 21, 300, sales);
        Person person6 = new Person("Pavel", 30, 1000, top);

        List<Person> persons = new ArrayList<>();
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);
        persons.add(person5);
        persons.add(person6);

        System.out.println(homework.countPersons(persons, 25, 130));

        System.out.println(homework.averageSalary(persons, dev));
        System.out.println(homework.averageSalary(persons, sales));
        System.out.println(homework.averageSalary(persons, top));

        System.out.println(homework.groupByDepartment(persons));

        System.out.println(homework.maxSalaryByDepartment(persons));

        System.out.println(homework.groupPersonNamesByDepartment(persons));

        System.out.println(homework.minSalaryPersons(persons));

    }
}