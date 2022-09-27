package ru.arman.testalpha.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.arman.testalpha.dto.UserDto;
import ru.arman.testalpha.error.UserNotFoundException;
import ru.arman.testalpha.error.UserResponseEntityExceptionHandler;
import ru.arman.testalpha.model.User;
import ru.arman.testalpha.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

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
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(UserResponseEntityExceptionHandler.class).build();
    }

    @Test
    public void getAllUsers_isAccepted() throws Exception {
        List<User> users = new ArrayList<>(Arrays.asList(user1, user2, user3));

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/users/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].name", is("Ivan")))
                .andExpect(jsonPath("$[0].surname", is("Nurgaliev")))
                .andExpect(jsonPath("$[2].patronymic", is("Petrovich")));
    }

    @Test
    public void getAllUsers_Empty_isAccepted() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    public void getAllUnbannedUsers_isAccepted() throws Exception {
        List<User> users = new ArrayList<>(Arrays.asList(user1, user2));

        Mockito.when(userService.getAllUnbannedUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/all-unbanned")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isAccepted())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[1].name", is("Ivan")))
                        .andExpect(jsonPath("$[0].surname", is("Nurgaliev")));
    }

    @Test
    public void getAllUnbannedUsers_Empty_isAccepted() throws Exception {
        Mockito.when(userService.getAllUnbannedUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/all-unbanned")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    public void createUser_isCreated() throws Exception {
        User user = User.builder()
                .id(4L)
                .login("alex")
                .password("pass")
                .name("Alexey")
                .surname("Alexeev")
                .patronymic("Alexeevich")
                .is_banned(false)
                .build();

        Mockito.when(userService.createUser(user)).thenReturn(user);

        String content = objectWriter.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.login", is("alex")));
    }

    @Test
    public void createUser_EmptyUser_isBadRequest() throws Exception {
        User user = new User();

        String content = objectWriter.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void createUser_EmptyLogin_isBadRequest() throws Exception {
        User user = User.builder()
                .id(4L)
                .login("")
                .password("pass")
                .name("Alexey")
                .surname("Alexeev")
                .patronymic("Alexeevich")
                .is_banned(false)
                .build();

        String content = objectWriter.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", contains("Login can't be empty")));
    }

    @Test
    public void createUser_EmptyInput_isBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUser_WrongInput_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("input"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changeUser_success() throws Exception {
        Long changeUserId = 2L;

        UserDto updateUser = UserDto.builder()
                .login("greg")
                .password("pass1")
                .name("Greg")
                .surname("Gregov")
                .patronymic("Gregovich")
                .build();

        User userAfterUpdate = User.builder()
                .id(2L)
                .login("greg")
                .password("pass1")
                .name("Greg")
                .surname("Gregov")
                .patronymic("Gregovich")
                .is_banned(false)
                .build();

        Mockito.when(userService.changeUser(changeUserId, updateUser)).thenReturn(userAfterUpdate);

        String content = objectWriter.writeValueAsString(updateUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/change/" + changeUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.login", is("greg")));
    }

    @Test
    public void changeUserNameAndSurname_success() throws Exception {
        Long changeUserId = 2L;

        UserDto updateUser = UserDto.builder()
                .name("Greg")
                .surname("Gregov")
                .build();

        User userAfterUpdate = User.builder()
                .id(changeUserId)
                .login("ivan")
                .password("pass")
                .name("Greg")
                .surname("Gregov")
                .patronymic("Ivanovich")
                .is_banned(false)
                .build();

        Mockito.when(userService.changeUser(changeUserId, updateUser)).thenReturn(userAfterUpdate);

        String content = objectWriter.writeValueAsString(updateUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/change/" + changeUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Greg")));
    }

    @Test
    public void changeUser_NotFound() throws Exception {
        Long changeUserId = 5L;

        UserDto updateUser = UserDto.builder()
                .login("greg")
                .password("pass1")
                .name("Greg")
                .surname("Gregov")
                .patronymic("Gregovich")
                .build();

        Mockito.when(userService.changeUser(changeUserId, updateUser))
                .thenThrow(new UserNotFoundException(String.format("User with id %s not found", changeUserId)));

        String content = objectWriter.writeValueAsString(updateUser);

            mockMvc.perform(MockMvcRequestBuilders
                            .put("/users/change/" + changeUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$", notNullValue()))
                    .andExpect(jsonPath("$.message",
                            is(String.format("User with id %s not found", changeUserId))));
    }

    @Test
    public void changeUser_EmptyInput_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/change/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void changeUser_WrongInput_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/change/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("asdasas"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void banUser_success() throws Exception {
        Long banUserId = 2L;

        User userAfterBan = User.builder()
                .id(2L)
                .login("ivan")
                .password("pass")
                .name("Ivan")
                .surname("Ivanov")
                .patronymic("Ivanovich")
                .is_banned(true)
                .build();

        Mockito.when(userService.banUser(banUserId)).thenReturn(userAfterBan);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/ban/" + banUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.login", is("ivan")))
                .andExpect(jsonPath("$.is_banned", is(true)));
    }

    @Test
    public void banUser_NotFound() throws Exception {
        Long banUserId = 2L;

        Mockito.when(userService.banUser(banUserId))
                .thenThrow(new UserNotFoundException(String.format("User with id %s not found", banUserId)));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/ban/" + banUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",
                        is(String.format("User with id %s not found", banUserId))));
    }
}