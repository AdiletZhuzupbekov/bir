package com.shtraf.bir.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")

    public String greeting(Model model){
        model.addAttribute("title", "Welcome");
        return "greeting";
    }

    @GetMapping("/main")
    public String mainPage(Model model){
        model.addAttribute("title", "Main Page");
        return "main";
    }
    @GetMapping("/about")
    public String aboutUs(Model model){
        model.addAttribute("title", "Информация");
        return "about";
    }
}
