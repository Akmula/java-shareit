package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataDuplicationException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        checkEmail(userDto);
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(int userId, UserDto userDto) {
        User oldUser = getUser(userId);

        if (!oldUser.getEmail().equals(userDto.getEmail())) {
            checkEmail(userDto);
        }

        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            oldUser.setEmail(userDto.getEmail());
        }

        User user = userRepository.save(oldUser);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUserById(int userId) {
        return UserMapper.toUserDto(getUser(userId));
    }

    @Override
    public List<UserDto> getUsers() {
        return UserMapper.usersToDto(userRepository.findAll());
    }

    @Override
    public void deleteUser(int userId) {
        getUser(userId);
        userRepository.deleteById(userId);
    }

    private User getUser(int userId) {
        return userRepository.findById(userId).
                orElseThrow(() -> new NotFoundException("Пользователь с id - " + userId + " не найден!"));
    }

    private void checkEmail(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.error("Данный email:{} уже используется!", userDto.getEmail());
            throw new DataDuplicationException("Данный email:" + userDto.getEmail() + " уже используется!");
        }
    }
}