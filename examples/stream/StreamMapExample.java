package examples.stream;

import java.util.List;

public class StreamMapExample {

    public static void main(String[] args) {
        List<String> names = List.of("java", "spring", "stream");

        List<String> upperNames = names.stream()
                .map(String::toUpperCase)
                .toList();

        System.out.println("원본 리스트: " + names);
        System.out.println("대문자 변환 결과: " + upperNames);
    }
}