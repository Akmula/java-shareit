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
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Creating user: {}", userDto);
        UserDto createdUserDto = userService.createUser(userDto);
        log.info("Created user: {}", createdUserDto);
        return createdUserDto;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable int userId,
                              @RequestBody UserDto userDto) {
        log.info("Updating user: {}", userDto);
        UserDto updatingUserDto = userService.updateUser(userId, userDto);
        log.info("Updated user: {}", updatingUserDto);
        return updatingUserDto;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        log.info("Getting user: {}", userId);
        UserDto gettingUserDto = userService.getUserById(userId);
        log.info("Getted user: {}", gettingUserDto);
        return gettingUserDto;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Getting all users");
        List<UserDto> allUsersDto = userService.getUsers();
        log.info("Getted all users: {}", allUsersDto);
        return allUsersDto;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.info("Deleting user: {}", userId);
        userService.deleteUser(userId);
        log.info("Deleted user: {}", userId);
    }
}