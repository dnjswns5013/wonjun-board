package com.example.myprj.member;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
	    try {
	        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
	        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(Map.of("error", "비밀번호가 일치하지 않습니다."));
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

	        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));

	    } catch (UsernameNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("error", "존재하지 않는 사용자입니다."));
	    }
	}
	
}
