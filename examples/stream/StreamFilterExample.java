package examples.stream;

import java.util.List;

public class StreamFilterExample {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

        List<Integer> evenNumbers = numbers.stream()
                .filter(n -> n % 2 == 0)
                .toList();

        System.out.println("원본 리스트: " + numbers);
        System.out.println("짝수만 추출: " + evenNumbers);
    }
}