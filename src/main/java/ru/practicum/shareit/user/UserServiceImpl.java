package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataDuplicationException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private Integer userId = 0;
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (isExistEmail(userDto.getEmail())) {
            throw new DataDuplicationException("Email already exists");
        }

        int id = getId();
        userDto.setId(id);
        User user = userRepository.addUser(UserMapper.dtoToUser(userDto));
        return UserMapper.userToDto(user);
    }

    @Override
    public UserDto updateUser(int userId, UserDto userDto) {

        User oldUser = userRepository.getUserById(userId);
        if (oldUser == null) {
            throw new NotFoundException("User not found");
        }

        User updatedUser = UserMapper.dtoToUser(userDto);
        updatedUser.setId(userId);

        if (!oldUser.getEmail().equals(userDto.getEmail())) {
            if (isExistEmail(userDto.getEmail())) {
                throw new DataDuplicationException("Email already exists");
            }
        }
        if (userDto.getName() == null) {
            updatedUser.setName(oldUser.getName());
        }
        if (userDto.getEmail() == null) {
            updatedUser.setEmail(oldUser.getEmail());
        }

        User user = userRepository.updateUser(updatedUser);
        return UserMapper.userToDto(user);
    }

    @Override
    public UserDto getUserById(int userId) {
        User user = userRepository.getUserById(userId);
        return UserMapper.userToDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.getUsers();
        return UserMapper.usersToDto(users);
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }

    private Integer getId() {
        userId++;
        return userId;
    }

    private boolean isExistEmail(String email) {
        return userRepository.getUsers().stream().anyMatch(user -> user.getEmail().equals(email));
    }
}