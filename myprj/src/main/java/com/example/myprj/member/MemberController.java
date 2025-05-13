package com.example.myprj.member;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myprj.common.ApiResponse;
import com.example.myprj.common.ResponseCode;
import com.example.myprj.member.dto.JoinRequest;
import com.example.myprj.member.dto.LoginRequest;
import com.example.myprj.member.dto.LoginResponse;
import com.example.myprj.security.CustomUserDetailsService;
import com.example.myprj.security.JwtUtil;
import com.example.myprj.token.RefreshToken;
import com.example.myprj.token.RefreshTokenMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService userDetailsService;
	private final JwtUtil jwtUtil;
	private final RefreshTokenMapper refreshTokenMapper;
	private final MemberMapper memberMapper;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
	    try {
	        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

	        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(new ApiResponse<>(
	                            false,
	                            ResponseCode.INVALID_PASSWORD.getCode(),
	                            ResponseCode.INVALID_PASSWORD.getMessage(),
	                            null
	                    ));
	        }

	        String accessToken = jwtUtil.generateToken(
	                userDetails.getUsername(),
	                userDetails.getAuthorities().iterator().next().getAuthority()
	        );

	        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

	        RefreshToken token = new RefreshToken();
	        token.setUsername(userDetails.getUsername());
	        token.setToken(refreshToken);
	        token.setExpiration(LocalDateTime.now().plusDays(7));
	        refreshTokenMapper.saveOrUpdate(token);

	        return ResponseEntity.ok(new ApiResponse<>(
	                true,
	                ResponseCode.SUCCESS.getCode(),
	                ResponseCode.SUCCESS.getMessage(),
	                new LoginResponse(accessToken, refreshToken)
	        ));

	    } catch (UsernameNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(new ApiResponse<>(
	                        false,
	                        ResponseCode.USER_NOT_FOUND.getCode(),
	                        ResponseCode.USER_NOT_FOUND.getMessage(),
	                        null
	                ));
	    }
	}

	
	@PostMapping("/join")
	public ResponseEntity<ApiResponse<?>> join(@RequestBody JoinRequest request) {
	    if (memberMapper.findByUsername(request.getUsername()) != null) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(
	                new ApiResponse<>(false,
	                        ResponseCode.DUPLICATE_USERNAME.getCode(),
	                        ResponseCode.DUPLICATE_USERNAME.getMessage(),
	                        null)
	        );
	    }

	    Member member = new Member();
	    member.setUsername(request.getUsername());
	    member.setPassword(passwordEncoder.encode(request.getPassword()));
	    member.setNickname(request.getNickname());
	    member.setEmail(request.getEmail());
	    member.setState("ACTIVE");
	    member.setRole("USER");

	    memberMapper.save(member);

	    return ResponseEntity.status(HttpStatus.CREATED).body(
	            new ApiResponse<>(true,
	                    ResponseCode.SUCCESS.getCode(),
	                    ResponseCode.SUCCESS.getMessage(),
	                    null)
	    );
	}


	
}
