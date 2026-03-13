package org.example.onlinevotingsystem.proxy;

import org.example.onlinevotingsystem.models.User;
import org.example.onlinevotingsystem.services.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceProxy implements IUserServiceProxy {
    private final UserService userService;

    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User findByUsername(String username) {
        // Add additional logic if needed
        System.out.println("Accessing user by username from proxy: " + username);
        return userService.findByUsername(username);
    }

    @Override
    public User findAuthenticatedUser() {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            throw new AccessDeniedException("You must be logged in to access profile page!");
        }
        return userService.findByUsername(user.getUsername());
    }
}
