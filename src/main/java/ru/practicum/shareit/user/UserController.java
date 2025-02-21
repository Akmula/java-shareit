package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto user) {
        log.info("Добавление пользователя: {}", user);
        UserDto createdUser = userService.createUser(user);
        log.info("Добавлен пользователь: {}", createdUser);
        return createdUser;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Integer userId,
                              @RequestBody UserDto user) {
        log.info("Обновление пользователя: {}", user);
        UserDto updatingUser = userService.updateUser(userId, user);
        log.info("Обновлен пользователь: {}", updatingUser);
        return updatingUser;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        log.info("Получение пользователя по id: {}", userId);
        UserDto gettingUser = userService.getUserById(userId);
        log.info("Получен пользователь: {}", gettingUser);
        return gettingUser;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        List<UserDto> allUsers = userService.getUsers();
        log.info("Получены пользователи: {}", allUsers);
        return allUsers;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.info("Удаление пользователя с id: {}", userId);
        userService.deleteUser(userId);
        log.info("Удален пользователь: {}", userId);
    }
}