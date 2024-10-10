package com.example.demo.controller;

import com.example.demo.dto.User;
import com.example.demo.dto.UserFormDto;
import com.example.demo.dto.UserLoginFormDto;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserFormInvalidException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.UserServiceImpl;
import com.example.demo.validator.UserValidator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("/user")
@Controller
public class UserController {

    private UserServiceImpl userService;
    private UserValidator userValidator;

    @Autowired
    public UserController(UserServiceImpl userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserAlreadyExistsException.class,UserNotFoundException.class,UserFormInvalidException.class})
    public String handleUserException(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @GetMapping("/me")
    public String getProfilePage() {
        return "profilePage";
    }


    @GetMapping("/register")
    public String getRegisterPage(@ModelAttribute("userFormDto") UserFormDto userFormDto) {
        return "registerForm";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userFormDto") UserFormDto userFormDto, BindingResult result) {
        if (result.hasErrors()) return "registerForm";
        userService.create(userFormDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginPage(@ModelAttribute("userLoginFormDto") UserLoginFormDto userLoginFormDto, String toUrl) {
        userLoginFormDto.setToUrl(toUrl != null ? toUrl : "/");
        return "loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userLoginFormDto") UserLoginFormDto userLoginFormDto,
            HttpServletResponse response, HttpSession session) {
        if (!checkValidUser(userLoginFormDto)) return "loginForm";
        checkCookie(userLoginFormDto, response);
        session.setAttribute("id", userLoginFormDto.getId());
        return "redirect:" + userLoginFormDto.getToUrl();
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    private boolean checkValidUser(UserLoginFormDto userLoginFormDto) {
        User foundUser = userService.findById(userLoginFormDto.getId());
        return foundUser.checkPwd(userLoginFormDto.getPwd());
    }

    private void checkCookie(UserLoginFormDto userLoginFormDto, HttpServletResponse response) {
        if (userLoginFormDto.isRememberMe()) {
            Cookie cooKie = new Cookie("id", userLoginFormDto.getId());
            cooKie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cooKie);
        } else {
            Cookie cooKie = new Cookie("id", "");
            cooKie.setMaxAge(0);
            response.addCookie(cooKie);
        }

    }

}
