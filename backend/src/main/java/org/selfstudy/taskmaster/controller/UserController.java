package org.selfstudy.taskmaster.controller;

import org.selfstudy.taskmaster.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://localhost:3030")
public class UserController {}
