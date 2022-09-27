package ru.arman.testalpha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.arman.testalpha.model.User;

import java.util.List;
import java.util.Optional;

@Repository(value = "JPA")
public interface UserJpaRepository extends JpaRepository<User, Long>, CustomizedUserRepository {
    @Override
    default List<User> findAllUsers() {
        System.out.println("JPA");
        return findAll();
    }

    @Override
    default User saveUser(User user) {
        return save(user);
    }

    @Override
    default Optional<User> findUserById(Long id) {
        return findById(id);
    }

    @Override
    default User updateUser(User user) {
        return save(user);
    }

    @Override
    default User banUser(User user) {
        return save(user);
    }
}
