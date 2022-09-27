package ru.arman.testalpha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String login;
    private String password;
    private String name;
    private String surname;
    private String patronymic;
    private Boolean is_banned;
}
