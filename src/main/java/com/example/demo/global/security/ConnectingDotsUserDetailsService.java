package com.example.demo.global.security;

import com.example.demo.dto.user.UserDto;
import com.example.demo.repository.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConnectingDotsUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 사용자 id를 받아서 조회함
    @Override
    public ConnectingDotsAuthUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto found = userRepository.selectByUserId(username);
        return new ConnectingDotsAuthUser(
                found.getUser_seq(),
                found.getId(),
                found.getPwd(),
                found.getEmail(),
                found.getSns(), // sns 전화번호로 사용
                List.of(new SimpleGrantedAuthority("ROLE_USER")) // 사용자 권한
        );
    }
}
