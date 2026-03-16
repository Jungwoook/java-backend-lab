package examples.stream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamGroupingExample {

    public static void main(String[] args) {
        List<String> fruits = List.of("apple", "banana", "apple", "orange", "banana", "apple");

        Map<String, Long> countMap = fruits.stream()
                .collect(Collectors.groupingBy(
                        fruit -> fruit,
                        Collectors.counting()
                ));

        System.out.println("원본 리스트: " + fruits);
        System.out.println("과일별 개수 집계: " + countMap);
    }
}