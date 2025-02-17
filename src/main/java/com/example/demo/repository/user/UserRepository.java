package com.example.demo.repository.user;

import com.example.demo.dto.user.UserDto;

public interface UserRepository {

    int count();

    boolean existsByUserId(String id);

    boolean existsByUserIdForUpdate(String id);

    boolean existsByEmail(String email);

    boolean existsByUserSeq(int userSeq);

    boolean existsByUserSeqForUpdate(int userSeq);

    UserDto selectByUserId(String id);

    UserDto selectByUserSeq(int userSeq);

    int insert(UserDto user);

    int update(UserDto user);

    int deleteByUserId(String id);

    int deleteByUserSeq(int userSeq);

    int deleteAll();
}
