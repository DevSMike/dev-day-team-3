# День Программиста В Яндекс Практикум 2023 
## Выполенение задания командой: Team3

## Содержание 
1. [Первое задание](#1задание)
2. [Второе задание](#2задание)
3. [Третье задание](#3задание)
4. [Четвёртое задание](#4задание)

   
<a name="1задание"></a>
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

<a name="2задание"></a>
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

<a name="3задание"></a>
### Третье задание 
Подобрать пароль, ориентируясь на ответы сервера.

Абстрактная версия решения представлена в классе **HttpClientController**.
Используем метод для подбора пароля и отправляем ответ на сервер.

Фрагмент класса *HttpClientController*:
```java
    @PostMapping("/task3")
    public String generatePassword() {
        log.info("HttpClientController: guessing a password");

        String pass = httpClientService.generatePassword();
        String result = null;

        try {
            result = client.post()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/3")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"password\": \"" + pass + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.info(responseBody);
        }
        return result;
    }
```

Метод, который используется для генерации пароля находится в классе **PasswordGenerationServiceImpl**:
```java
   public String hackPass() {
        long a = 0L;
        long b = passToLong("ffffffff");
        long m = (a + b) / 2;

        while (true) {
            int response = PasswordGeneratorRequestsUtil.tryPass(longToPass(m));

            if (response == 1) {
                b = m;
                m = (a + b) / 2;
            } else if (response == -1) {
                a = m;
                m = (a + b) / 2;
            } else if (response == 0) {
                break;
            } else {
                throw new RuntimeException("tryPass(" + longToPass(m) + ") вернул " + response);
            }
        }
        return longToPass(m);
    }
```

Основой для данного алгоритма служит метод *tryPass(String password)*. Данный метод отправляет запрос на сервер, чтобы узнать: текущий символ >/< нужного. В ответ сервер выбрасывает исключение, которое парсится в строку, оттуда вытаскивается нужная информация: *>pass* или *<pass*. Это передается в алгоритм *tryPass(String password)*:

```java
    public static Integer tryPass(String password) {

        String result = "";
        try {
            result = WEB_CLIENT.post()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/3")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"password\": \"" + password + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest e) {
            String errorMessage = e.getResponseBodyAsString();

            if (errorMessage.contains(">")) {
                result = errorMessage.substring(errorMessage.lastIndexOf(">"), errorMessage.lastIndexOf(">") + 5);
            } else if (errorMessage.contains("<")) {
                result = errorMessage.substring(errorMessage.lastIndexOf("<"), errorMessage.lastIndexOf("<") + 5);
            } else {
                log.error("Unsupported exceptions happens in PasswordGeneratorRequestsUtil tryPass(): {}", errorMessage);
            }
        }

        log.debug("PasswordGeneratorRequestsUtil: tryPass answer: {}", result);

        if (result == null) result = "";

        return decodeAnswerToInt(result);
    }
```

Таким образом, пароль генерируется максимально быстро: в худшем случае понадобится 66 запросов.

<a name="4задание"></a>
### Четвёртове задание

Расшифровать слово на кирилице, записанное в неправильной кодировке, которое содержится в HTML коде.
Пример слова: **Øèðîêàÿ ýëåêòðèôèêàöèÿ**.

Абстрактная версия решения представлена в классе **HttpClientController**.
Сначала получаем от сервера HTML код задания. Далее выполняется парсинг этого кода, чтобы выловить зашифрованное слово. После этого происходить расшифровка с помощью алгоритма.

Фрагмент класса *HttpClientController*:
```java
    @PostMapping("/task4")
    public String sendDecodedString() {
        log.info("HttpClientController: congratulate with dev day");

        String htmlText = null;

        try {
            htmlText = client.get()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/4")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.info(responseBody);
        }

        String decoded = httpClientService.decodeCongratulations(htmlText);
        String result = "";

        try {
            result = client.post()
                    .uri("http://ya.praktikum.fvds.ru:8080/dev-day/task/4")
                    .header("AUTH_TOKEN", "e26d3434-c970-482a-b055-e2a55a364581")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"congratulation\": \"" + decoded + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException.BadRequest ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.info(responseBody);
        }

        return result;
    }
```

Метод, который используется для декодирования слова находится в классе **DecoderRequesterUtil**:
```java
    public static String getDecodedString (String codedString) throws IOException {

        HttpResponse response = makeDecodePostRequest(codedString);
        HttpEntity entity = response.getEntity();
        String responseText = EntityUtils.toString(entity);

        String coder = ExtractFromStringUtil.extractValueFromDecoderResponse(responseText);
        EntityUtils.consume(entity);
        return  ExtractFromStringUtil.extractValueWithAnswerFromCoder(responseText, coder+":ISO-8859-15");
    }
```

Идея алгоритма лежит в том, чтобы с помощью программы отправить запрос на сервер, на котором содержится универсальный декодер, распарсить ответ и вернуть результат. 
Метод, который составляет запрос, аналгичный запросу с браузера находится в этом же классе: 

```java
private static HttpResponse makeDecodePostRequest(String codedString) throws IOException {

        HttpPost httpPost = new HttpPost("https://2cyr.com/decode/?lang=ru");

        httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpPost.setHeader("Accept-Language", "ru,en;q=0.9");
        httpPost.setHeader("Cache-Control", "max-age=0");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Cookie", "PHPSESSID=f6a0a706428c4727411af372d7566ed7");
        httpPost.setHeader("Dnt", "1");
        httpPost.setHeader("Origin", "https://2cyr.com");
        httpPost.setHeader("Referer", "https://2cyr.com/decode/?lang=ru");
        httpPost.setHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"YaBrowser\";v=\"23\"");
        httpPost.setHeader("Sec-Ch-Ua-Mobile", "?1");
        httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Android\"");
        httpPost.setHeader("Sec-Fetch-Dest", "document");
        httpPost.setHeader("Sec-Fetch-Mode", "navigate");
        httpPost.setHeader("Sec-Fetch-Site", "same-origin");
        httpPost.setHeader("Sec-Fetch-User", "?1");
        httpPost.setHeader("Sec-Gpc", "1");
        httpPost.setHeader("Upgrade-Insecure-Requests", "1");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36");

        // Создаем payload
        String payload = "lang=ru&authid=&authpw=&text=" + codedString +"&ident=:WINDOWS-1251:ISO-8859-15&identselected=OK&sample=&src=WINDOWS-1251&dsp=WINDOWS-1252&prf=";

        // Устанавливаем payload как тело запроса
        httpPost.setEntity(new StringEntity(payload));

        // Выполняем запрос и получаем ответ
        return  HTTP_CLIENT.execute(httpPost);
    }
```


