package ru.kata.spring.boot_security.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class InitDataBase implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public InitDataBase(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // Создаём роли
            Role adminRole = new Role("ADMIN");
            Role userRole = new Role("USER");
            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            // Создаём пользователей
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);

            User admin = new User();
            admin.setUsername("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(adminRoles);
            admin.setAge(28);

            User user = new User();
            user.setUsername("User");
            user.setEmail("user@gmail.com");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRoles(userRoles);
            user.setAge(32);

            userRepository.save(admin);
            userRepository.save(user);
        }
    }
}