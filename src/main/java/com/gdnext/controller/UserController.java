package com.gdnext.controller;

import com.gdnext.entity.User;
import com.gdnext.feign.ExternalService;
import com.gdnext.repository.UserRepository;
import com.gdnext.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/form")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/missing-fields/{userId}")
    public Map<String, Boolean> getMissingFields(@PathVariable Long userId) {
        return userService.getMissingFields(userId);
    }

    @GetMapping("/fetch-all-users")
    public List<User> getMissingFields() {
        return userService.getAllUsers();
    }

    @PostMapping("/submit/{userId}")
    public String submitForm(@PathVariable Long userId, @RequestBody Map<String, String> formData) {
        return userService.submitForm(userId, formData);
    }

}

