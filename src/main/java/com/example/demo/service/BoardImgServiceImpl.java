package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BoardImgServiceImpl {

    @Value("${boardImgLocation}")
    private String boardImgLocation;


}
