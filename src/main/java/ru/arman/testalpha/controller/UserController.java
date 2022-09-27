package ru.arman.testalpha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.arman.testalpha.dto.UserDto;
import ru.arman.testalpha.error.UserNotFoundException;
import ru.arman.testalpha.model.User;
import ru.arman.testalpha.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
    }

    @GetMapping("/all-unbanned")
    public ResponseEntity<List<User>> getAllUnbannedUsers() {
        List<User> users = userService.getAllUnbannedUsers();
        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getFieldErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        try {
            User user1 = userService.createUser(user);
            return new ResponseEntity<>(user1, HttpStatus.CREATED);
        } catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/change/{id}")
    public ResponseEntity<?> changeUser(@PathVariable Long id, @RequestBody UserDto user) throws UserNotFoundException {
        try {
            User user1 = userService.changeUser(id, user);
            return new ResponseEntity<>(user1, HttpStatus.OK);
        } catch (HttpClientErrorException.BadRequest e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/ban/{id}")
    public ResponseEntity<User> banUser(@PathVariable Long id) throws UserNotFoundException {
        User user = userService.banUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
