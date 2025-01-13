//package com.example.demo.application.user;
//
//import com.example.demo.dto.user.UserDto;
//import com.example.demo.repository.mybatis.user.UserDaoImpl;
//import org.springframework.security.core.userdetails.User;
////import com.example.demo.dto.user.User;
//import com.example.demo.dto.user.UserFormDto;
//import com.example.demo.dto.user.UserUpdatedFormDto;
//import com.example.demo.exception.technology.InternalServerError;
//import com.example.demo.exception.user.UserAlreadyExistsException;
//import com.example.demo.exception.user.UserFormInvalidException;
//import com.example.demo.exception.user.UserNotFoundException;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class UserServiceImpl implements UserDetailsService {
//
////    private final UserDaoImpl userDao;
////    private final PasswordEncoder passwordEncoder;
////
////    @Autowired
////    public UserServiceImpl(UserDaoImpl userDao, PasswordEncoder passwordEncoder) {
////        this.passwordEncoder = passwordEncoder;
////        this.userDao = userDao;
////    }
////
////    @Override
////    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
////        var foundUser = userDao.selectByName(username);
////
////        if (foundUser == null) {
////            throw new UsernameNotFoundException("해당 이름(아이디)의 유저를 찾지 못했습니다.");
////        }
////
////        return User.withUsername(foundUser.getId())
////                   .password(foundUser.getPwd())
////                   .build();
////    }
////
////    public int count() {
////        return userDao.count();
////    }
////
////    public void create(UserFormDto dto) {
////        int rowCnt = 0;
////        try {
////            dto.setPwd(passwordEncoder.encode(dto.getPwd()));
////            rowCnt = userDao.insert(dto);
////            if (rowCnt != 1) {
////                throw new InternalServerError();
////            }
////        } catch (DuplicateKeyException e) {
////            throw new UserAlreadyExistsException();
////        } catch (DataIntegrityViolationException e) {
////            throw new UserFormInvalidException();
////        }
////    }
////
////    public UserDto findById(String id) {
////        var foundUser = userDao.selectById(id);
////        if (foundUser == null) {
////            throw new UserNotFoundException();
////        }
////
////        return foundUser;
////    }
////
////    public List<UserDto> findAll() {
////        return userDao.selectAll();
////    }
////
////    public void modify(UserUpdatedFormDto dto) {
////        int rowCnt = 0;
////
////        try {
////            rowCnt = userDao.update(dto);
////            if (rowCnt != 1) {
////                throw new InternalServerError();
////            }
////        } catch (DuplicateKeyException e) {
////            throw new UserAlreadyExistsException();
////        } catch (DataIntegrityViolationException e) {
////            throw new UserFormInvalidException();
////        }
////    }
////
////    public void remove(String id) {
////        var foundUser = userDao.selectById(id);
////        if (foundUser == null) {
////            throw new UserNotFoundException();
////        }
////
////        int rowCnt = 0;
////        rowCnt = userDao.deleteById(id);
////        if (rowCnt != 1) {
////            throw new InternalServerError();
////        }
////    }
////
////    @Transactional(rollbackFor = Exception.class)
////    public void removeAll() {
////        int totalCnt = userDao.count();
////        int rowCnt = userDao.deleteAll();
////
////        if (rowCnt == totalCnt) {
////            throw new InternalServerError();
////        }
////    }
//
//}
