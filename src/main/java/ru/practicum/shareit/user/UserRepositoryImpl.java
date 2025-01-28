package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(int userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getUsers() {
        return users.values().stream().toList();
    }

    @Override
    public void deleteUser(int userId) {
        users.remove(userId);
    }
}