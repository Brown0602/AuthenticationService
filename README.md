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

    final AuthenticationService authenticationService;

    @Autowired
    public ControllerAuthentication(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    public String authentication(@RequestBody UserDTO userDTO){
        try {
            return authenticationService.authentication(userDTO.getUser_login(), userDTO.getUser_password(), userDTO.getUser_email());
        }catch (Exception exception){
            return "Пользователь с такими данными не найден";
        }
    }

}
```
```ControllerAuthentication``` использует ```AuthenticationService``` для проверки введенных данных пользователя с БД. Если вдруг данные не совпадают, то выбрасывается исключение, где оно перехватывается и отобразится предупреждение. 
## 3. Забыл данные для входа
```java
@RestController
public class ControllerForgotPassword {

    JdbcTemplate jdbcTemplate;

    final FindEmailService findEmailService;
    final ForgotDataService forgotDataService;
    final MailConfirmationService mailConfirmationService;
    final FindCodeConfirmationService findCodeConfirmationService;
    final DeleteCodeConfirmationService deleteCodeConfirmationService;

    @Autowired
    public ControllerForgotPassword(JdbcTemplate jdbcTemplate, FindEmailService findEmailService, ForgotDataService forgotDataService, MailConfirmationService mailConfirmationService, FindCodeConfirmationService findCodeConfirmationService, DeleteCodeConfirmationService deleteCodeConfirmationService){
        this.jdbcTemplate = jdbcTemplate;
        this.findEmailService = findEmailService;
        this.forgotDataService = forgotDataService;
        this.mailConfirmationService = mailConfirmationService;
        this.findCodeConfirmationService = findCodeConfirmationService;
        this.deleteCodeConfirmationService = deleteCodeConfirmationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> forgotPassword(){
        return ResponseEntity.ok(jdbcTemplate.query("SELECT * FROM restfulapiusers", new UserRowMapper()));
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public String forgotPassword(@RequestBody UserDTO userDTO, HttpSession httpSession) throws MessagingException, IOException {
        if (userDTO.getUser_login() == null && userDTO.getUser_password() == null && userDTO.getUser_email() != null && userDTO.getCode_confirmation() == null) {
            try{
                findEmailService.findEmail(userDTO.getUser_email());
                mailConfirmationService.mailConfirmation(userDTO.getUser_email());
            }catch (Exception exception){
                return "Пользователь с таким email не существует";
            }
        } else if (userDTO.getUser_login() == null && userDTO.getUser_password() == null && userDTO.getUser_email() == null && userDTO.getCode_confirmation() != null) {
            try {
                findCodeConfirmationService.findCodeConfirmation(userDTO.getCode_confirmation());
                httpSession.setAttribute("code_confirmation", userDTO.getCode_confirmation());
            }catch (Exception exception){
                return "Неверный код водтверждения";
            }
        }else {
            forgotDataService.forgotData(userDTO, httpSession);
            deleteCodeConfirmationService.deleteCodeConfirmation(String.valueOf(httpSession.getAttribute("code_confirmation")));
        }
        return "";
    }
}
```
Для восстановления данных ```ControllerForgotPassword``` использует ```FindEmailService``` для поиска пользователя по введенному email.
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
```MailConfirmationService``` использует ```CodeGenerationService``` для генерации 6-значного кода подтверждения и сохраняя его в БД используя ```SaveCodeConfirmationService```. После идет отправление на почту.
```java
@Repository
public class MailConfirmationRepo implements MailConfirmationService {

    SaveCodeConfirmationService saveCodeConfirmationService;
    CodeGenerationService codeGenerationService;

    @Autowired
    public MailConfirmationRepo(SaveCodeConfirmationService saveCodeConfirmationService, CodeGenerationService codeGenerationService){
        this.saveCodeConfirmationService = saveCodeConfirmationService;
        this.codeGenerationService = codeGenerationService;
    }

    @Value("${mail.password}")
    private String mailPassword;
    @Value("${mail.myEmail}")
    private String myEmail;

    @Override
    public String mailConfirmation(String email) throws IOException, MessagingException {

        Properties properties = new Properties();
        properties.load(MailConfirmationRepo.class.getClassLoader().getResourceAsStream("application.properties"));

        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(myEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Подтверждение изменения пароля");
        String code_confirmation = codeGenerationService.codeGeneration();
        saveCodeConfirmationService.saveCodeConfirmation(code_confirmation, String.valueOf(email.hashCode()));
        message.setText("Здравствуйте!\nДля подтверждения изменения пароля введите в поле формы указанный код: " + code_confirmation);
        Transport transport = mailSession.getTransport();
        transport.connect(myEmail, mailPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        return "Введите код подтверждения";
    }
}
```
```java
@Repository
public class CodeGenerationRepo implements CodeGenerationService {
    @Override
    public String codeGeneration() {
        StringBuilder codeConfirmationStringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            double doubleNumber = Math.random() * 9;
            int intNumber = (int)doubleNumber;
            codeConfirmationStringBuilder.append(intNumber);
        }
        return String.valueOf(codeConfirmationStringBuilder);

    }
}
```
```java
@Repository
public class SaveCodeConfirmationRepo implements SaveCodeConfirmationService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public SaveCodeConfirmationRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveCodeConfirmation(String codeConfirmation, String email) {
        String sqlQuery = "UPDATE restfulapiusers SET code_confirmation = ? WHERE user_email = ?";
        jdbcTemplate.update(sqlQuery, codeConfirmation, email);
    }
}
```
После получения кода подтверждения пользователь должен ввести его в поле формы подтверждения кода. После ввода кода подтверждения в форму, ```ControllerForgotPassword``` использует ```FindCodeConfirmationSevice``` для сверки с кодом подтверждения в БД.
```java
@Repository
public class FindCodeConfirmationRepo implements FindCodeConfirmationService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public FindCodeConfirmationRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String findCodeConfirmation(String codeConfirmation) {
        String sqlQuery = "SELECT * FROM restfulapiusers WHERE code_confirmation = ?";
        jdbcTemplate.queryForObject(sqlQuery, new UserRowMapper(), codeConfirmation);
        return sqlQuery;
    }
}
```
Используя ```HttpSession``` временно сохраняется в сессии, для последующего удаления кода подтверждения после изменения данных. Далее ```ControllerForgotPassword``` использует ```ForgotDataService``` для изменения данных входа.
```java
@Repository
public class ForgotDataRepo implements ForgotDataService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public ForgotDataRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String forgotData(UserDTO userDTO, HttpSession httpSession) {
        String sqlQuery = "UPDATE restfulapiusers SET user_login = ?, user_password = ? WHERE code_confirmation = ?";
        jdbcTemplate.update(sqlQuery, String.valueOf(userDTO.getUser_login().hashCode()), String.valueOf(userDTO.getUser_password().hashCode()), httpSession.getAttribute("code_confirmation"));
        return sqlQuery;
    }
}
```
После изменения данных входа, код подтверждения удаляется из БД сервисом ```DeleteCodeConfirmationService```.
```java
@Repository
public class DeleteCodeConfirmationRepo implements DeleteCodeConfirmationService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public DeleteCodeConfirmationRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteCodeConfirmation(String code_confirmation) {
        String sqlQuery = "UPDATE restfulapiusers SET code_confirmation = ? WHERE code_confirmation = ?";
        jdbcTemplate.update(sqlQuery, null, code_confirmation);
    }
}
```
