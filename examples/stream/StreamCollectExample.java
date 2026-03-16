package examples.stream;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamCollectExample {

    public static void main(String[] args) {
        List<String> fruits = List.of("apple", "banana", "apple", "orange");

        List<String> fruitList = fruits.stream()
                .collect(Collectors.toList());

        Set<String> fruitSet = fruits.stream()
                .collect(Collectors.toSet());

        System.out.println("원본 리스트: " + fruits);
        System.out.println("collect(toList()) 결과: " + fruitList);
        System.out.println("collect(toSet()) 결과: " + fruitSet);
    }
}