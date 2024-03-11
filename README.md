# Документация к сервису аутентификации
## 1. Регистрация нового пользователя
```java
@RestController
public class ControllerRegistration {

    final FindEmailService findEmailService;
    final ValidationService validationService;

    @Autowired
    public ControllerRegistration(FindEmailService findEmailService, ValidationService validationService) {
        this.findEmailService = findEmailService;
        this.validationService = validationService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@RequestBody UserDTO userDTO){
        try {
            findEmailService.findEmail(userDTO.getUser_email());
            return "Пользователь с таким email уже существует";
        }catch (Exception exception) {
            validationService.validation(userDTO.getUser_login(), userDTO.getUser_password(), userDTO.getUser_email());
        }
        return "";
    }
}
```
Класс ```ControllerRegistration``` использует ```FindEmailService``` для поиска пользователя. Если пользователя с данным email нет в БД, то выбрасывается исключения, которое перехватывается блоком "сatch".
```java
@Repository
public class FindEmailRepo implements FindEmailService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public FindEmailRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void findEmail(String email) {
        String sqlQuery = "SELECT * FROM restfulapiusers WHERE user_email = ?";
        jdbcTemplate.queryForObject(sqlQuery, new UserRowMapper(), String.valueOf(email.hashCode()));
    }
}
```
После перехвата исключения идет проверка валидности данных с помощью ```ValidationService```.
```java
@Repository
public class ValidationRepo implements ValidationService {

    final private RegistrationService registrationService;

    @Autowired
    public ValidationRepo(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public String validation(String user_login, String user_password, String user_email) {
        String capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String chars = "'!@#$%^&*()_=+-;:,.<>?";
        for (int i = 0; i < user_password.length(); i++){
            for (int j = 0; j < chars.length(); j++){
                if (user_password.charAt(i) == chars.charAt(j)){
                    for (int l = 0; l < user_password.length(); l++){
                        for (int q = 0; q < capitals.length(); q++){
                            if (user_password.charAt(l) == capitals.charAt(q)){
                                return registrationService.registration(user_login, user_password, user_email);
                            }
                        }
                    }
                }
            }
        }
        return "Пароль не соотвествует требованиям";
    }
}
```
```ValidationService``` использует ```RegistrationService``` для создания нового пользователя в БД, если пароль соотвествует требованиям.
```java
@Repository
public class RegistrationRepo implements RegistrationService {

    final private JdbcTemplate jdbcTemplate;

    @Autowired
    public RegistrationRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String registration(String user_login, String user_password, String user_email) {
        String sqlQuery = "INSERT INTO restfulapiusers(user_login, user_password, user_email) VALUES(?, ?, ?)";
        jdbcTemplate.update(sqlQuery, user_login.hashCode(), user_password.hashCode(), user_email.hashCode());
        return sqlQuery;
    }
}
```
## 2. Аутентификация пользователя
```java
@RestController
public class ControllerAuthentication {

    final private AuthenticationService authenticationService;

    @Autowired
    public ControllerAuthentication(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    public String authentication(@RequestBody UserDTO userDTO) throws MessagingException, IOException {
        return authenticationService.authentication(userDTO.getUser_login(), userDTO.getUser_password(), userDTO.getUser_email());

    }

}
```
```ControllerAuthentication``` использует ```AuthenticationService``` 
