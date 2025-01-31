package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {

    User addUser(User user);

    User updateUser(User user);

    User getUserById(int userId);

    List<User> getUsers();

    void deleteUser(int userId);
}