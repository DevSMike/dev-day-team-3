# День Программиста В Яндекс Практикум 2023 
## Выполенение задания командой: Team3

### Первое задание

Зарегистировать команду на выполенение задания, а также в заголовке MAIN_ANSWER к запросу ответить на главный вопрос вселенной.

Абстрактная версия решения представлена в классе HttpClientController. 
Метод обрабатывает тело запроса DevBody, который содержит необходимую метаинформацию для регистрации команды, потом отвечает на поставленный вопрос и регистрирует команду. 


Класс DevBody и его значение:
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

Пример содержания Json файла, который десериализуется в DevBody:
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
Фрагмент класса HttpClientController:
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
  
