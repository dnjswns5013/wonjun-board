package com.example.myprj.member.dto;

import lombok.Getter;

@Getter
public class JoinRequest {
    private String username;
    private String password;
    private String nickname;
    private String email;
}
