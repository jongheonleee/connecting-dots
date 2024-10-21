package com.example.demo.application.service.comment;


import com.example.demo.repository.mybatis.comment.CommentDaoImpl;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl {

    private final CommentDaoImpl commentDao;

    public CommentServiceImpl(CommentDaoImpl commentDao) {
        this.commentDao = commentDao;
    }

    public int count() {
        return commentDao.count();
    }
}
