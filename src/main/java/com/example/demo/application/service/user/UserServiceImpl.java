package com.example.demo.application.service.user;

import com.example.demo.dto.user.UserDto;
import com.example.demo.repository.mybatis.user.UserDaoImpl;
import org.springframework.security.core.userdetails.User;
//import com.example.demo.dto.user.User;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserDaoImpl userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDaoImpl userDao, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var foundUser = userDao.selectByName(username);

        if (foundUser == null) {
            throw new UsernameNotFoundException("사용자를 찾지 못했습니다.");
        }

        return User.withUsername(foundUser.getId())
                   .password(foundUser.getPwd())
                   .build();
    }

    public int count() {
        return userDao.count();
    }

    public void create(UserFormDto dto) {
        int rowCnt = 0;
        try {
            dto.setPwd(passwordEncoder.encode(dto.getPwd()));
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

    public UserDto findById(String id) {
        var foundUser = userDao.selectById(id);
        if (foundUser == null) {
            throw new UserNotFoundException("해당 " + id + "를 가진 사용자를 찾을 수 없습니다.");
        }

        return foundUser;
    }

    public List<UserDto> findAll() {
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
