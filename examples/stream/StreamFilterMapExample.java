package examples.stream;

import java.util.List;

public class StreamFilterMapExample {

    public static void main(String[] args) {
        List<String> names = List.of("alice", "bob", "anna", "charlie", "alex");

        List<String> result = names.stream()
                .filter(name -> name.startsWith("a"))
                .map(String::toUpperCase)
                .toList();

        System.out.println("원본 리스트: " + names);
        System.out.println("a로 시작하는 이름을 대문자로 변환: " + result);
    }
}