package com.eecs4413.groupe.userservice.service;

import com.eecs4413.groupe.userservice.exception.*;
import com.eecs4413.groupe.userservice.model.entity.User;
import com.eecs4413.groupe.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository _userRepository;

    public UserService(UserRepository userRepository) {
        _userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return _userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return _userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public int getStorePoints(UUID id) {
        User user = _userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return user.getStorePoints();
    }

    public User addUser(User user) {
        if (_userRepository.existsByEmail(user.getEmail())) {
            throw new EmailNotUniqueException(user.getEmail());
        }

        user.setId(null);

        return _userRepository.save(user);
    }

    public User updateUser(UUID id, User user) {
        User currentUser = _userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getEmail().equals(currentUser.getEmail()) && _userRepository.existsByEmail(user.getEmail())) {
            throw new EmailNotUniqueException(user.getEmail());
        }

        user.setId(id);

        return _userRepository.save(user);
    }

    public User updateEmail(UUID id, String newEmail) {
        User currentUser = _userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (!newEmail.equals(currentUser.getEmail()) && _userRepository.existsByEmail(newEmail)) {
            throw new EmailNotUniqueException(newEmail);
        }

        currentUser.setEmail(newEmail);

        return _userRepository.save(currentUser);
    }

    public User updateStorePoints(UUID id, int quantity) {
        User user = _userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        int currentStorePoints = user.getStorePoints();

        if (currentStorePoints + quantity < 0) throw new NotEnoughPointsException(quantity*-1, currentStorePoints);
        user.setStorePoints(user.getStorePoints() + quantity);

        return _userRepository.save(user);
    }

    public void deleteUserById(UUID id) {
        if(!_userRepository.existsById(id)) throw new UserNotFoundException(id);

        _userRepository.deleteById(id);
    }
}
