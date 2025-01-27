package com.example.demo.controller.reply;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.service.reply.ReplyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser
@WebMvcTest(ReplyController.class)
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReplyService replyService;

    @BeforeEach
    void setUp() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(replyService);
    }


}