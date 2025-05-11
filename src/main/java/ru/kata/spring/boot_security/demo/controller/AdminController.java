package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/new";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, @RequestParam(required = false) List<Long> roleIds) {
        user.setRoles(roleIds.stream()
                .map(id -> roleRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findByIdWithRoles(id));
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/edit";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user, @RequestParam(required = false) List<Long> roleIds) {
        user.setRoles(roleIds.stream()
                .map(id -> roleRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}