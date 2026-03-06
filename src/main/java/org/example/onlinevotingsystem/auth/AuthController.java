package org.example.onlinevotingsystem.auth;

import jakarta.validation.Valid;
import org.example.onlinevotingsystem.FacadePattern.IFacade;
import org.example.onlinevotingsystem.models.Role;
import org.example.onlinevotingsystem.models.User;
import org.example.onlinevotingsystem.builder.UserResponse;
import org.example.onlinevotingsystem.proxy.IUserServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;

@Controller
public class AuthController {
    @Autowired
    private IFacade votingSystemFacade;

    @Autowired
    private IUserServiceProxy userServiceProxy;  // Proxy for UserService


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, Model model) {
        votingSystemFacade.createUserAndAdminApprovalNotification(user);
        model.addAttribute("message", "Registration successful. Please wait for admin approval.");
        return "login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "login";
    }

    @GetMapping("/index")
    public String showIndexPage() {

        return "redirect:/polls";
    }

    @GetMapping(value = "/profile")
    public String getUserProfile(Model model) {
        // org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // accessed using proxy object
        User user = userServiceProxy.findAuthenticatedUser();
        model.addAttribute("currentPage", "profile");
        model.addAttribute("user", UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .build()
        );
        return "profile";
    }
}
