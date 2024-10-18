package com.example.demo.presentation;

import com.example.demo.dto.user.User;
import com.example.demo.dto.user.UserFormDto;
import com.example.demo.dto.user.UserLoginFormDto;
import com.example.demo.application.exception.user.UserAlreadyExistsException;
import com.example.demo.application.exception.user.UserFormInvalidException;
import com.example.demo.application.exception.user.UserNotFoundException;
import com.example.demo.application.service.user.UserServiceImpl;
import com.example.demo.application.validator.user.UserValidator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    @InitBinder("userFormDto")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserAlreadyExistsException.class,UserNotFoundException.class,UserFormInvalidException.class})
    public String handleUserException(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @GetMapping("/myPage")
    public String getProfilePage(Model model, HttpServletRequest request) {
        // 세선에서 유저 아이디 조회하기 - 이 부분 스프링 시큐리티로 빼버리기
        if (!isLogin(request)) {
            String toUrl = request.getRequestURI();
            return "redirect:/user/login?toUrl=" + toUrl;
        }

        // 유저 정보 조회하기
        String userId = findUserIdOnSession(request);
        findUserById(model, userId);

        return "profilePage";
    }


    @GetMapping("/register")
    public String getRegisterPage(@ModelAttribute("userFormDto") UserFormDto userFormDto) {
        return "registerForm";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userFormDto") UserFormDto userFormDto, BindingResult result) {
        if (result.hasErrors())
            return "registerForm";

        userService.create(userFormDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginPage(@ModelAttribute("userLoginFormDto") UserLoginFormDto userLoginFormDto, HttpServletRequest request, String toUrl) {
        readCookie(request, userLoginFormDto);
        userLoginFormDto.setToUrl(toUrl);
        return "loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userLoginFormDto") UserLoginFormDto userLoginFormDto,
            HttpServletResponse response, HttpSession session) {
        if (!checkValidUser(userLoginFormDto))
            return "loginForm";

        checkCookie(userLoginFormDto, response);
        session.setAttribute("id", userLoginFormDto.getId());
        return "redirect:" + userLoginFormDto.getToUrl();
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    private void findUserById(Model model, String id) {
        User foundUser = userService.findById(id);
        model.addAttribute("user", foundUser);
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

    private boolean isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session != null && session.getAttribute("id") != null;
    }

    private String findUserIdOnSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("id");
    }

    private void readCookie(HttpServletRequest request, UserLoginFormDto userLoginFormDto) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("id")) {
                    userLoginFormDto.setId(cookie.getValue());
                    userLoginFormDto.setRememberMe(true);
                }
            }
        }
    }

}
