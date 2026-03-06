package org.example.onlinevotingsystem.controllers;

import org.example.onlinevotingsystem.repositories.UserRepository;
import org.example.onlinevotingsystem.services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PollController {

    @Autowired
    private PollService pollService;

    @Autowired
    private UserRepository voterRepository;

    @Autowired
    private NotificationService notificationService;


}
