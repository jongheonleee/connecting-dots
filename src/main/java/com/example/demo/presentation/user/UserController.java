package com.example.demo.presentation.user;

import com.example.demo.application.board.BoardServiceImpl;
import com.example.demo.dto.user.UserFormDto;
import com.example.demo.exception.user.UserAlreadyExistsException;
import com.example.demo.exception.user.UserFormInvalidException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.application.user.UserServiceImpl;
import com.example.demo.validator.user.UserValidator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.SessionAttribute;



/**
 * 회원 개발 목록
 * - 1. 회원가입 ✅
 * - 2. 로그인/로그아웃 ✅
 * - 3. JWT & Outh2 소셜로그인
 * - 4. 프로필 정보 조회
 * - 5. 회원 정보 수정
 * - 6. 회원 탈퇴
 * - 7. 시큐리티 인증/인가
 */

@RequestMapping("/user")
@Controller
public class UserController {

    private final UserServiceImpl userService;
    private final UserValidator userValidator;
    private final BoardServiceImpl boardService;

    public UserController(UserServiceImpl userService, UserValidator userValidator,
                          BoardServiceImpl boardService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.boardService = boardService;
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
    public String getProfilePage(Model model, @SessionAttribute String id, HttpServletRequest request) {
        findUserById(model, id);
//        findBoardsByUserId(model, id);
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
    public String getLoginPage(HttpServletRequest request, Model model) {
        // 💥 쿠키 처리
        readCooke(request, model);
        return "loginForm";
    }

//    private void findBoardsByUserId(Model model, String id) {
//        var boards = boardService.findByUserId(id);
//        model.addAttribute("boards", boards);
//    }


    private void findUserById(Model model, String id) {
        var foundUser = userService.findById(id);
        model.addAttribute("user", foundUser);
    }

    private void readCooke(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;

        for (Cookie cookie : cookies) {
            if (cookie.getAttribute("id") != null) {
                String userId = cookie.getAttribute("id");
                System.out.println("userId = " + userId);
                model.addAttribute("id", userId);
            }
        }

    }

}
