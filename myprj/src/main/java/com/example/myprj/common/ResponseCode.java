package com.example.myprj.common;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS("SUCCESS", "요청이 정상 처리되었습니다."),
    INVALID_PASSWORD("ERROR_001", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND("ERROR_002", "존재하지 않는 사용자입니다."),
    DUPLICATE_USERNAME("ERROR_003", "이미 존재하는 아이디입니다.");
	
    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
