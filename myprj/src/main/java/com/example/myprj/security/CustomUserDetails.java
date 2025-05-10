package com.example.myprj.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.myprj.member.Member;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    // 사용자의 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole()));
    }

    // 비밀번호 반환
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 아이디 반환
    @Override
    public String getUsername() {
        return member.getUsername();
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return member.getState().equals("ACTIVE");
    }
}
