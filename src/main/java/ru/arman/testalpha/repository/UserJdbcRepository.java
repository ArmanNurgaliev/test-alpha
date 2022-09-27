package ru.arman.testalpha.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.arman.testalpha.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository(value = "JDBC")
public class UserJdbcRepository implements CustomizedUserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final String SQL_INSERT_USER = "INSERT INTO users (login, password, name, surname, patronymic, is_banned) VALUES(?,?,?,?,?,?)";
    private final String SQL_SELECT_ALL_USERS = "SELECT * FROM users";
    private final String SQL_FIND_USER_BY_ID = "SELECT * FROM users WHERE id=?";
    private final String SQL_UPDATE_USER = "UPDATE users SET login=?, password=?, name=?, surname=?, patronymic=?, is_banned=? WHERE id=?";
    private final String SQL_BAN_USER = "update users set is_banned=true where id = ?";

    @Autowired
    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAllUsers() {
        System.out.println("JDBC");
        return jdbcTemplate.query(SQL_SELECT_ALL_USERS,
                new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User saveUser(User user) {

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(SQL_INSERT_USER,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getPatronymic());
            statement.setBoolean(6, user.getIs_banned());
            return statement;
        }, holder);

        long id = Objects.requireNonNull(holder.getKey()).longValue();
        user.setId(id);

        return user;
    }

    @Override
    public Optional<User> findUserById(Long id) {
       return jdbcTemplate.query(SQL_FIND_USER_BY_ID,
                new BeanPropertyRowMapper<>(User.class),
                id)
                .stream().findAny();
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(SQL_UPDATE_USER,
                user.getLogin(), user.getPassword(), user.getName(),
                user.getSurname(), user.getPatronymic(), user.getIs_banned(),
                user.getId());

        return user;
    }

    @Override
    public User banUser(User user) {
        jdbcTemplate.update(SQL_BAN_USER,
                user.getId());

        return user;
    }
}
