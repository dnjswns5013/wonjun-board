package com.example.myprj.member;

import lombok.Data;

@Data
public class Member {

    private int id; // 기본키
    private String username; // 아이디
    private String password; // 비밀번호
    private String nickname; // 닉네임
    private String profile; // 프로필 이미지
    private String email; // 이메일
    private String state; // 상태 (ACTIVE, WITHDRAWN, SUSPENDED, BANNED)
    private String role; // 권한 (USER, BOARD_MANAGER, ADMIN)
    private String createdAt; // 가입일시
    
}
