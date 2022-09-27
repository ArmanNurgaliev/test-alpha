package ru.arman.testalpha.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Login can't be empty")
    private String login;
    @NotBlank(message = "Password can't be empty")
    private String password;
    @NotBlank(message = "Name can't be empty")
    private String name;
    @NotBlank(message = "Surname can't be empty")
    private String surname;
    @NotBlank(message = "Patronymic can't be empty")
    private String patronymic;
    private Boolean is_banned = false;

}
