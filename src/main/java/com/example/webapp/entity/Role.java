package com.example.webapp.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 역할 Enum
 * Spring Security의 권한 시스템과 연동해서 사용
 */
@Getter
@RequiredArgsConstructor
public enum Role {

    /**
     * 일반 사용자
     * 기본적인 서비스 이용 권한
     */
    USER ("ROLE_USER", "일반 사용자"),

    /**
     *  관리자
     *  시스템 관리 및 모든 기능 접근 권한
     */
    ADMIN("ROLE_ADMIN", "관리자");

    /**
     * Spring Security에서 사용되는 권한명
     * ROLE_" 접두사가 포함됨
     */
    private final String authority;

    /**
     * 사용자에게 표시될 역할명
     */
    private final String displayName;

    /**
     * 관리자 여부 확인
     *
     * @return 관리자 여부
     */
    public boolean isAdmin(){
        return this == ADMIN;
    }

    /**
     * 일반 사용자 권한 여부 확인
     *
     * @return 일반 사용자 여부
     */
    public boolean isUser(){
        return this == USER;
    }

    /**
     * 문자열로부터 Role 찾기
     *
     * @param authority 권한명 또는 역할명
     * @return Role (찾지 못하면 USER 반환)
     */
    public static Role fromString(String authority){
        if(authority == null)
            return USER;

//        "ROLE_" 접두사 제거
        String cleanAuthority = authority.replace("ROLE_", "").toUpperCase();

        try{
            return Role.valueOf(cleanAuthority);
        } catch(IllegalArgumentException e){
            return USER;
        }
    }

    /**
     * 권한명으로부터 Role 찾기
     *
     * @param authority 권한명 (예: "ROLE_USER")
     * @return Role (찾지 못하면 USER 반환)
     */
    public static Role fromAuthority(String authority){
        for(Role role: values()){
            if(role.authority.equals(authority)){
                return role;
            }
        }
        return USER;
    }


}
