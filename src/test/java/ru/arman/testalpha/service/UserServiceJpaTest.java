package ru.arman.testalpha.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import ru.arman.testalpha.dto.UserDto;
import ru.arman.testalpha.error.UserNotFoundException;
import ru.arman.testalpha.model.User;
import ru.arman.testalpha.repository.CustomizedUserRepository;
import ru.arman.testalpha.repository.UserJpaRepository;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles(profiles = {"jpatest"})
class UserServiceJpaTest {

    @InjectMocks
    private UserService userService;
    @MockBean
    private CustomizedUserRepository customizedUserRepository;

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

    @BeforeEach
    void setUp() {
        userService = new UserService(customizedUserRepository);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsers_success() {
        List<User> users = new ArrayList<>(Arrays.asList(user1, user2, user3));

        Mockito.when(customizedUserRepository.findAllUsers()).thenReturn(users);

        assertEquals(users, userService.getAllUsers());
    }

    @Test
    public void getAllUsers_Empty_success() {
        Mockito.when(customizedUserRepository.findAllUsers()).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), userService.getAllUsers());
    }

    @Test
    public void createUser_Success() {
        User user = User.builder()
                .login("alex")
                .password("pass")
                .name("Alexey")
                .surname("Alexeev")
                .patronymic("Alexeevich")
                .is_banned(false)
                .build();

        Mockito.when(customizedUserRepository.saveUser(user)).thenReturn(user);

        assertEquals(user, userService.createUser(user));
    }

    @Test
    public void changeUser_success() throws UserNotFoundException {
        Long changeUserId = 1L;

        UserDto updateUser = UserDto.builder()
                .login("greg")
                .password("pass1")
                .name("Greg")
                .surname("Gregov")
                .patronymic("Gregovich")
                .build();

        User userAfterUpdate = User.builder()
                .id(1L)
                .login("greg")
                .password("pass1")
                .name("Greg")
                .surname("Gregov")
                .patronymic("Gregovich")
                .is_banned(false)
                .build();

        Mockito.when(customizedUserRepository.findUserById(changeUserId)).thenReturn(Optional.ofNullable(user1));
        Mockito.when(customizedUserRepository.updateUser(user1)).thenReturn(userAfterUpdate);

        assertEquals(userAfterUpdate, userService.changeUser(changeUserId, updateUser));
    }

    @Test
    public void changeUser_WithWrongId_UserNotFound() {
        Long changeUserId = 1L;

        UserDto updateUser = UserDto.builder()
                .login("greg")
                .password("pass1")
                .name("Greg")
                .surname("Gregov")
                .patronymic("Gregovich")
                .build();

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.changeUser(changeUserId, updateUser));

        String expectedMessage = "User with id " + changeUserId + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void banUser_success() throws UserNotFoundException {

        Mockito.when(customizedUserRepository.findUserById(user1.getId())).thenReturn(Optional.ofNullable(user1));

        User bannedUser = user1;
        bannedUser.setIs_banned(true);

        Mockito.when(customizedUserRepository.banUser(any(User.class))).thenReturn(bannedUser);

        assertEquals(bannedUser, userService.banUser(1L));
    }

    @Test
    public void banUser_WrongId_NotFound() {
        Long banUserId = 1L;

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.banUser(banUserId));

        String expectedMessage = "User with id " + banUserId + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
