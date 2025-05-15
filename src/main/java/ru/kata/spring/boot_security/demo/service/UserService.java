package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User saveUser(User user);
    User updateUser(User user, List<Long> roleIds);
    void deleteUser(Long id);
    List<User> findAll();
    User findById(Long id);
    User findByIdWithRoles(Long id);
}