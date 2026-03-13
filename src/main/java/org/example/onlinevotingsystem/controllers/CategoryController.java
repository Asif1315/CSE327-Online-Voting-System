package org.example.onlinevotingsystem.controllers;

import org.example.onlinevotingsystem.models.Category;
import org.example.onlinevotingsystem.models.Notification;
import org.example.onlinevotingsystem.models.User;
import org.example.onlinevotingsystem.repositories.UserRepository;
import org.example.onlinevotingsystem.services.CategoryService;
import org.example.onlinevotingsystem.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRepository voterRepository;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/admin-category-create")
    public String showCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-create"; // Thymeleaf template
    }

    @PostMapping("/admin-category-create")
    public String createCategory(@ModelAttribute Category category, Model model) {
        categoryService.createCategory(category);
        model.addAttribute("message", "Category created successfully!");
        return "category-create";
    }

    @GetMapping("/categrories")
    public String showAdminIndexPage(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        User currentUser = voterRepository.findByUsername(principal.getName()).get();
        List<Notification> notifications = notificationService.getAllNotifications(currentUser);

        // unread notifications count
        long unreadCount = notifications.stream().filter(n -> !n.isRead()).count();
        model.addAttribute("unreadcount", unreadCount);
        model.addAttribute("notifications", notifications);

        return "categoriesview";
    }



}
