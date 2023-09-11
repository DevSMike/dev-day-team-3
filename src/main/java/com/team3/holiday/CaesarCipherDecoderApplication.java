//package com.team3.holiday;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.http.*;
//import org.springframework.web.client.RestTemplate;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//@SpringBootApplication
//public class CaesarCipherDecoderApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(CaesarCipherDecoderApplication.class, args);
//
//        // URL для GET-запроса
//        String getUrl = "http://ya.praktikum.fvds.ru:8080/dev-day/task/2";
//
//        // HTTP-header AUTH_TOKEN
//        String authToken = "e26d3434-c970-482a-b055-e2a55a364581";
//
//        // Создаем RestTemplate для выполнения HTTP-запросов
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Создаем HTTP-заголовок с токеном
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("AUTH_TOKEN", authToken);
//
//        // Создаем HTTP-запрос типа GET с заголовками
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        // Выполняем GET-запрос
//        ResponseEntity<String> response = restTemplate.exchange(getUrl, HttpMethod.GET, entity, String.class);
//
//        // Извлекаем строку "encoded" и "offset" из ответа
//        String responseBody = response.getBody();
//        String textFromHtml = extractValueFromResponse(responseBody, "encoded");
//        String encodedText = textFromHtml.substring(1, textFromHtml.lastIndexOf(",") - 1);
//        //int offset = Integer.parseInt(extractValueFromResponse(responseBody, "offset"));
//        int offset = Integer.parseInt(textFromHtml.substring(textFromHtml.lastIndexOf(" ") + 2));
//        // Декодируем текст
//        String decodedText = decodeCaesarCipher(encodedText, offset);
//
//        // Создаем JSON для POST-запроса
//        String postRequestBody = "{\"decoded\": \"" + decodedText + "\"}";
//
//        // Создаем HTTP-запрос типа POST с заголовками
//        HttpHeaders postHeaders = new HttpHeaders();
//        postHeaders.setContentType(MediaType.APPLICATION_JSON);
//        postHeaders.set("AUTH_TOKEN", authToken);
//
//        HttpEntity<String> postEntity = new HttpEntity<>(postRequestBody, postHeaders);
//
//        // URL для POST-запроса
//        String postUrl = "http://ya.praktikum.fvds.ru:8080/dev-day/task/2";
//
//        // Выполняем POST-запрос
//        ResponseEntity<String> postResponse = restTemplate.exchange(postUrl, HttpMethod.POST, postEntity, String.class);
//
//        System.out.println("Ответ на POST-запрос: " + postResponse.getBody());
//    }
//
//    // Метод для извлечения значения из JSON-строки по ключу
//    private static String extractValueFromResponse(String responseBody, String key) {
//        Document document = Jsoup.parse(responseBody);
//
//        Elements elements = document.select("code span");
//        for (Element element : elements) {
//            String spanText = element.text();
//            if (spanText.contains("\"" + key + "\"")) {
//                int startIndex = spanText.indexOf(":") + 2;
//                int endIndex = spanText.length() - 2;
//                return spanText.substring(startIndex, endIndex);
//            }
//        }
//
//        return "";
//    }
//
//    // Метод для декодирования шифра Цезаря
//    private static String decodeCaesarCipher(String encodedText, int offset) {
//        StringBuilder decodedText = new StringBuilder();
//
//        for (char character : encodedText.toCharArray()) {
//            if (Character.isLetter(character)) {
//                char base = Character.isLowerCase(character) ? 'a' : 'A';
//                char decodedChar = (char) (((character - base - offset + 26) % 26) + base);
//                decodedText.append(decodedChar);
//            } else {
//                // Если символ не буква, добавляем его без изменений
//                decodedText.append(character);
//            }
//        }
//
//        return decodedText.toString();
//    }
//}
