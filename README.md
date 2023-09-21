# День Программиста В Яндекс Практикум 2023 
## Выполенение задания командой: Team3

### Первое задание

Зарегистрировать команду на выполнение задания, а также в заголовке **MAIN_ANSWER** к запросу ответить на главный вопрос вселенной.

Абстрактная версия решения представлена в классе **HttpClientController**. 
Метод обрабатывает тело запроса **DevBody**, который содержит необходимую метаинформацию для регистрации команды, потом отвечает на поставленный вопрос и регистрирует команду. 


Класс *DevBody* и его значение:
```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevBody {
    //Лист участников
    List<Dev> participants;
    //Название команды
    String name;
    //Ссылка на репозиторий 
    String gitHubUrl;
}
```

Пример содержания Json файла, который десериализуется в *DevBody*:
```json
{"name": "Team3", "gitHubUrl":"https://github.com/DevSMike/dev-day-team-3", "participants":
[
  {
    "email": "email1@yandex.ru",
    "cohort": "java_33",
    "firstName": "Alexander",
    "lastName": "Kosarev"
  },
  {
    "email": "email2@yandex.ru",
    "cohort": "java_29",
    "firstName": "Dmitry",
    "lastName": "Novik"
  },
  {
    "email": "email3@yandex.ru",
    "cohort": "java_16",
    "firstName": "Mikhail",
    "lastName": "Lukashev"
  }
]}
```
Фрагмент класса *HttpClientController*:
```java
    @PostMapping("/task1")
    public Mono<DevBodyInfoDto> makeRegister(@RequestBody DevBody body) {
        log.info("HttpClientController: making a register");
        DevBodyDto bodyDto = httpClientService.registerUser(body);
        //async query
        return client.post()
                .uri("http://ya.praktikum.fvds.ru:8080/dev-day/register")
                .header("MAIN_ANSWER", "42")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bodyDto.getRequestBodyJson())
                .retrieve()
                .bodyToMono(DevBodyInfoDto.class);
    }
```

### Второе задание

Расшифровать шифр Цезяра. Пример пароля, который содержится в HTML коде: 
```json 
{"encoded": "TMHQ M RUZQ DQMOF OAPUZS PMK", "offset": "12"}
 ```

Абстрактная версия решения представлена в классе **HttpClientController**.
Сначала получаем от сервера HTML код задания. Далее выполняется парсинг этого кода, чтобы выловить пароль. После этого происходить расшифровка с помощью алгоритма.

Фрагмент класса *HttpClientController*:
```java
@GetMapping("/task2")
    public String decodeMessage() {
        log.info("HttpClientController: task2 get HTML string code");
        String resultOfGet = null;

        try {
            resultOfGet = client.get()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/2")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.debug(responseBody);
        }

        String decoded = httpClientService.decodeMessage(resultOfGet);
        String result = null;

        try {
            result = client.post()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/2")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(decoded)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.debug(responseBody);
        }

        return result;
    }
```

Метод, который используется для декодирования шифра Цезаря находится в классе **DecodeCaesarCipherUtil**:
```java
    public static String decodeCaesarCipher(String encodedText, int offset) {
        StringBuilder decodedText = new StringBuilder();

        for (char character : encodedText.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isLowerCase(character) ? 'a' : 'A';
                char decodedChar = (char) (((character - base - offset + 26) % 26) + base);
                decodedText.append(decodedChar);
            } else {
                // Если символ не буква, добавляем его без изменений
                decodedText.append(character);
            }
        }
        return decodedText.toString();
    }
}
```
