package ru.arman.testalpha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.arman.testalpha.dto.UserDto;
import ru.arman.testalpha.error.UserNotFoundException;
import ru.arman.testalpha.model.User;
import ru.arman.testalpha.repository.CustomizedUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final CustomizedUserRepository customizedUserRepository;

    /*public UserService(Environment environment, ApplicationContext context) {
        String selected_repo = environment.getProperty("SELECTED_REPO");
        System.out.println("SELECTED_REPO: " + selected_repo);
        this.customizedUserRepository = context.getBean(selected_repo, CustomizedUserRepository.class);
    }*/

   /* @Autowired
    public UserService(@Value("${selected.repo}") String selected_repo, ApplicationContext context) {
        System.out.println("SELECTED_REPO: " + selected_repo);
        this.customizedUserRepository = context.getBean(selected_repo, CustomizedUserRepository.class);
    }*/

    @Autowired
    public UserService(@Value("#{${selected.repo:JPA}}") CustomizedUserRepository customizedUserRepository) {
        this.customizedUserRepository = customizedUserRepository;
    }

    public List<User> getAllUsers() {
        return customizedUserRepository.findAllUsers();
    }

    public User createUser(User user) {
        return customizedUserRepository.saveUser(user);
    }

    public User changeUser(Long id, UserDto user) throws UserNotFoundException {
        User userFromDB = getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));

        if (user.getLogin() != null)
            userFromDB.setLogin(user.getLogin());
        if (user.getPassword() != null)
            userFromDB.setPassword(user.getPassword());
        if (user.getName() != null)
            userFromDB.setName(user.getName());
        if (user.getSurname() != null)
            userFromDB.setSurname(user.getSurname());
        if (user.getPatronymic() != null)
            userFromDB.setPatronymic(user.getPatronymic());
        if (user.getIs_banned() != null)
            userFromDB.setIs_banned(user.getIs_banned());

        customizedUserRepository.updateUser(userFromDB);

        return userFromDB;
    }


    public User banUser(Long id) throws UserNotFoundException {
        User userFromDB = getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));
        userFromDB.setIs_banned(true);

        return customizedUserRepository.banUser(userFromDB);
    }

    public List<User> getAllUnbannedUsers()  {
        return customizedUserRepository.findAllUsers().
                stream().filter(u -> !u.getIs_banned()).collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) throws UserNotFoundException {
        return customizedUserRepository.findUserById(id);
    }
}
