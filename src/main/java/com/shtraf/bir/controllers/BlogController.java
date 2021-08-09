package com.shtraf.bir.controllers;


import com.shtraf.bir.models.Message;
import com.shtraf.bir.models.User;
import com.shtraf.bir.repos.MessageRepo;
import com.shtraf.bir.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@Controller
public class BlogController {

    @Autowired
    private MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/blog")
    public String blogController(@RequestParam (required = false, defaultValue = "") String filter, Model model){
        Iterable<Message> messages = messageRepo.findAll();
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
        model.addAttribute("title", "Страница блога");
        model.addAttribute("title","Блог");
        model.addAttribute("messages", messages);
        model.addAttribute("filter",filter);
        return "blog";
    }
    @PostMapping("/blog")
    public String addMessage(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()){
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
            System.out.println("Error of binding result");
        }else {
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFilename));

                message.setFilename(resultFilename);
            }

            model.addAttribute("message", null);
            messageRepo.save(message);
        }

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "blog";
    }
}

//                need to fix  navbar username
//
//@ControllerAdvice
//class GlobalControllerAdvice {
//    @Autowired
//    private UserRepo userRepo;
//
//    @ModelAttribute("naveName")
//    public String getNameNav(Model model) {
//        model.addAttribute("users", userRepo.findAll());
//        return "some-value";
//    }
//}
