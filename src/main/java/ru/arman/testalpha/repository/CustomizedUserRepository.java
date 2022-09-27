package ru.arman.testalpha.repository;

import org.springframework.stereotype.Repository;
import ru.arman.testalpha.error.UserNotFoundException;
import ru.arman.testalpha.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomizedUserRepository {
    List<User> findAllUsers();

    User saveUser(User user);

    Optional<User> findUserById(Long id) throws UserNotFoundException;

    User updateUser(User user);

    User banUser(User user);
}
