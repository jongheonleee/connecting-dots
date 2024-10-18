package com.example.demo.application.service.user;

import com.example.demo.repository.mybatis.user.UserDaoImpl;
import com.example.demo.dto.user.User;
import com.example.demo.dto.user.UserFormDto;
import com.example.demo.dto.user.UserUpdatedFormDto;
import com.example.demo.application.exception.global.InternalServerError;
import com.example.demo.application.exception.user.UserAlreadyExistsException;
import com.example.demo.application.exception.user.UserFormInvalidException;
import com.example.demo.application.exception.user.UserNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl {

    private final UserDaoImpl userDao;

    @Autowired
    public UserServiceImpl(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    public int count() {
        return userDao.count();
    }

    public void create(UserFormDto dto) {
        int rowCnt = 0;
        try {
            rowCnt = userDao.insert(dto);
            if (rowCnt != 1) {
                throw new InternalServerError("DB에 정상적으로 반영도지 못했습니다. 현재 적용된 로우수는 " + rowCnt + "입니다.");
            }
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException("이미 존재하는 아이디입니다. " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new UserFormInvalidException("입력하신 데이터가 올바르지 않습니다. " + e.getMessage());
        }
    }

    public User findById(String id) {
        var foundUser = userDao.selectById(id);
        if (foundUser == null) {
            throw new UserNotFoundException("해당 " + id + "를 가진 사용자를 찾을 수 없습니다.");
        }

        return foundUser;
    }

    public List<User> findAll() {
        return userDao.selectAll();
    }

    public void modify(UserUpdatedFormDto dto) {
        int rowCnt = 0;

        try {
            rowCnt = userDao.update(dto);
            if (rowCnt != 1) {
                throw new InternalServerError("DB에 정상적으로 반영도지 못했습니다. 현재 적용된 로우수는 " + rowCnt + "입니다.");
            }
        } catch (DuplicateKeyException e) {
            throw new UserAlreadyExistsException("이미 존재하는 아이디입니다. " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new UserFormInvalidException("입력하신 데이터가 올바르지 않습니다. " + e.getMessage());
        }
    }

    public void remove(String id) {
        var foundUser = userDao.selectById(id);
        if (foundUser == null) {
            throw new UserNotFoundException("해당 " + id + "를 가진 사용자를 찾을 수 없습니다.");
        }

        int rowCnt = 0;
        rowCnt = userDao.deleteById(id);
        if (rowCnt != 1) {
            throw new InternalServerError("DB에 정상적으로 반영도지 못했습니다. 현재 적용된 로우수는 " + rowCnt + "입니다.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeAll() {
        int totalCnt = userDao.count();
        int rowCnt = userDao.deleteAll();

        if (rowCnt == totalCnt) {
            throw new InternalServerError("DB에 정상적으로 반영도지 못했습니다. 현재 적용된 로우수는 " + rowCnt + "입니다.");
        }
    }

}
