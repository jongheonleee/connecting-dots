package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserFormDto;
import com.example.demo.dto.UserLoginFormDto;
import com.example.demo.exception.InternalServerError;
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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @ExceptionHandler({UserAlreadyExistsException.class, UserNotFoundException.class, UserFormInvalidException.class})
    public String handleException() {
        return "error";
    }

    @GetMapping("/register")
    public String getRegisterPage(@ModelAttribute("userFormDto") UserFormDto userFormDto) {
        return "registerForm";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userFormDto") UserFormDto userFormDto, BindingResult result) {
        // 회원 필드들 유효성 검증 처리
        // 회원등록 처리
        // 성공적으로 등록함 -> 로그인 상태 유지하고 메인 페이지 이동
        // 실패함 -> 에러 메시지 로그인 폼 페이지에서 보여줌
        if (result.hasErrors()) {
            return "registerForm";
        }

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
        // 입력받은 아이디, 비밀번호 확인
        // 아이디로 조회된 회원의 비밀번호와 입력받은 비밀번호가 일치하면 로그인 성공
        // 세션 생성 후 이전 페이지로 이동
        // 그게 아니면 로그인 실패
        // 쿠키 체크되었으면 쿠키에 아이디 기록
        UserDto foundUser = userService.findById(userLoginFormDto.getId());
        if (!foundUser.getId().equals(userLoginFormDto.getId())) {
            return "loginForm";
        }

        if (userLoginFormDto.isRememberMe()) {
            // 쿠키에 아이디 기록
            Cookie cooKie = new Cookie("id", userLoginFormDto.getId());
            cooKie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cooKie);
        } else {
            // 쿠키에 아이디 삭제
            Cookie cooKie = new Cookie("id", "");
            cooKie.setMaxAge(0);
            response.addCookie(cooKie);
        }

        session.setAttribute("id", userLoginFormDto.getId());
        return "redirect:" + userLoginFormDto.getToUrl();
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
