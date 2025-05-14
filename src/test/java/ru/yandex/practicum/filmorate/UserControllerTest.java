package ru.yandex.practicum.filmorate;



/*
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private Validator validator;
    private User user;

    @Autowired
    private UserController userController;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setLogin("login");
        user.setEmail("email@mail.ru");
        user.setBirthday(LocalDate.of(1996, 5, 7));
    }

    @Test
    void testValidUser() {
        User createdUser  = userController.create(user);
        List<User> users = userController.getAll();
        assertEquals(2, users.size(), "Сервер не вернул всех пользователей");
        assertEquals(createdUser, users.get(1), "Созданный пользователь не совпадает с полученным");
    }

    @Test
    void testValidForLoginWithSpaces() {
        user.setLogin("log in");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(!violations.isEmpty(), "Должны быть нарушения валидации для логина с пробелом");
    }

    @Test
    void testValidForValidUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Должны отсутствовать нарушения валидации для валидного пользователя");
    }

    @Test
    void testValidEmailUser() {
        user.setEmail("emailmail.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Некорректная электронная почта", violations.iterator().next().getMessage());
    }

    @Test
    void testValidBirthdayUser() {
        user.setBirthday(LocalDate.now().plusMonths(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Дата рождения не может быть в будущем", violations.iterator().next().getMessage());
    }

    @Test
    void testValidNameNull() {
        user.setName(null);
        String loginUser = user.getLogin();
        User createdUser  = userController.create(user);
        assertEquals(loginUser, createdUser.getName());
    }

}
*/
