package com.example.myprj.token;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshToken {
    private Long id;
    private String username;
    private String token;
    private LocalDateTime expiration;
}
