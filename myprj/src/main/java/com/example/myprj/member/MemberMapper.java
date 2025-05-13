package com.example.myprj.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

	Member findByUsername(String username);
	void save(Member member);

}
