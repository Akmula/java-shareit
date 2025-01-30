package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataDuplicationException;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Integer userId = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> usersEmail = new HashSet<>();

    @Override
    public User addUser(User user) {
        final String email = user.getEmail();
        if (usersEmail.contains(email)) {
            throw new DataDuplicationException("User with email " + email + " already exists");
        }
        int id = getId();
        user.setId(id);
        users.put(id, user);
        usersEmail.add(email);
        return user;
    }

    @Override
    public User updateUser(User user) {
        final String email = user.getEmail();
        users.computeIfPresent(user.getId(), (id, u) -> {
            if (!email.equals(u.getEmail())) {
                if (usersEmail.contains(email)) {
                    throw new DataDuplicationException("User with email " + email + " already exists");
                }
                usersEmail.remove(u.getEmail());
                usersEmail.add(email);
            }
            return user;
        });
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
        User deletedUser = getUserById(userId);
        users.remove(userId);
        usersEmail.remove(deletedUser.getEmail());
    }

    private Integer getId() {
        userId++;
        return userId;
    }
}