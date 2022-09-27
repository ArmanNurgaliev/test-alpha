package ru.arman.testalpha.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.arman.testalpha.model.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
        "SELECTED_REPO=JDBC",
        "SERVER_PORT=8080"
})
@Testcontainers
class UserJdbcRepositoryTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private UserJdbcRepository userJdbcRepository;

    private User user1 = User.builder()
            .id(1L)
            .login("arman")
            .password("pass")
            .name("Arman")
            .surname("Nurgaliev")
            .patronymic("Sembaevich")
            .is_banned(false)
            .build();

    private User user2 = User.builder()
            .id(2L)
            .login("ivan")
            .password("pass")
            .name("Ivan")
            .surname("Ivanov")
            .patronymic("Ivanovich")
            .is_banned(false)
            .build();

    private User user3 = User.builder()
            .id(3L)
            .login("petr")
            .password("pass")
            .name("Petr")
            .surname("Petrov")
            .patronymic("Petrovich")
            .is_banned(true)
            .build();

    private final String SQL_SELECT_ALL_USERS = "SELECT * FROM users";

    @BeforeEach
    void setUp() {
        userJdbcRepository = new UserJdbcRepository(jdbcTemplate);
    }

 /*   @Test
    public void testPostgresSqlModule() {
        System.out.println(postgreSQLContainer.getJdbcUrl());
        System.out.println(postgreSQLContainer.getTestQueryString());
    }

    @Test
    public void findAllUsers_EmptyList_success() {
        List<User> users = userJdbcRepository.findAllUsers();
        assertEquals(0, users.size());
    }

    @Test
    public void findAllUsers_success() {
        List<User> users = Arrays.asList(user1, user2, user3);
*//*
        Mockito.when(jdbcTemplate.query(SQL_SELECT_ALL_USERS, new BeanPropertyRowMapper<>(User.class)))
                .thenReturn(users);*//*

       *//* Mockito.when(jdbcTemplate.query((PreparedStatementCreator) any(String.class),
                new BeanPropertyRowMapper<User>()))
                .thenReturn(users);*//*

        List<User> resultList = userJdbcRepository.findAllUsers();
        System.out.println(resultList);
        assertEquals(3, resultList.size());
    }

    @Test
    public void saveUser_success() {
        Mockito.when(jdbcTemplate.update((PreparedStatementCreator) any(String.class),
                new GeneratedKeyHolder()))
                .thenReturn(1);


        User resultUser = userJdbcRepository.saveUser(user1);

        assertThat(user1).isEqualTo(resultUser);
    }*/

   /* @Test
    public void getUserById_success() {
        Long userId = 1L;
        Mockito.when(jdbcTemplate.query(any(String.class),
                new BeanPropertyRowMapper<>(User.class),
                userId))
                .thenReturn(user1);
    }*/
}