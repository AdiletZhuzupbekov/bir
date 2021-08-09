package com.shtraf.bir.controllers;

import com.shtraf.bir.models.Role;
import com.shtraf.bir.models.User;
import com.shtraf.bir.repos.UserRepo;
import com.shtraf.bir.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){

        model.addAttribute("title","Пользователи");
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{userId}")
    public String userEditForm(@PathVariable Long userId, Model model) {
        User user = userRepo.findById(userId).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("title","Изминение ролей");
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
            ){

        userService.saveUser(user, username, form);
        return  "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }
    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email
    ){
        userService.updateProfile(user, password, email);
        return "redirect:/user/profile";
    }


}

