package ru.practicum.shareit.user;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static List<UserDto> usersToDto(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).toList();
    }
}