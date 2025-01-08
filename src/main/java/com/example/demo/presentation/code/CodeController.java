package com.example.demo.presentation.code;

import com.example.demo.application.code.CommonCodeServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/code")
public class CodeController {

    private final CommonCodeServiceImpl commonCodeService;


}
