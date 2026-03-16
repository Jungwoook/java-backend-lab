package examples.stream;

import java.util.List;

public class StreamObjectExample {

    public static void main(String[] args) {
        List<Person> people = List.of(
                new Person("Kim", 20),
                new Person("Lee", 17),
                new Person("Park", 25),
                new Person("Choi", 19)
        );

        List<String> adultNames = people.stream()
                .filter(person -> person.getAge() >= 20)
                .map(Person::getName)
                .toList();

        System.out.println("전체 사람 목록:");
        people.forEach(person -> System.out.println(person.getName() + " (" + person.getAge() + ")"));

        System.out.println("성인 이름 목록: " + adultNames);
    }

    static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}