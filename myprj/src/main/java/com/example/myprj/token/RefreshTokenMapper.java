package com.example.myprj.token;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper {
    void saveOrUpdate(RefreshToken token);
    RefreshToken findByUsername(String username);
    void deleteByUsername(String username);
}
