package com.example.webapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;  // ✅ 이게 맞는 import
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 3.0 설정 클래스
 * API 문서 자동 생성 및 JWT 인증 설정
 */
@Configuration
public class SwaggerConfig {

    private static final String BEARER_TOKEN_SECURITY_SCHEME = "Bearer Authentication";

    /**
     * OpenAPI 3.0 설정
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement()
                        .addList(BEARER_TOKEN_SECURITY_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_TOKEN_SECURITY_SCHEME, createAPIKeyScheme()));
    }

    /**
     * API 기본 정보 설정
     */
    private Info apiInfo() {
        return new Info()
                .title("ToDo Webapplication API")
                .description("ToDo webApplication API Swagger 문서입니다" +
                        "**토큰 사용 방법:**\n" +
                        "1. POST /api/auth/login 으로 로그인\n" +
                        "2. 응답의 accessToken 값을 복사\n" +
                        "3. 우측 상단 'Authorize' 버튼 클릭\n" +
                        "4. 'Bearer Authentication' 필드에 토큰 입력 (Bearer 접두사 없이)\n" +
                        "5. 'Authorize' 클릭하여 인증 완료")
                .version("1.0.0")
                .contact(new Contact()
                        .name("차승환")
                        .email("greatcloud13@gmail.com"));
    }

    /**
     * JWT Bearer Token 보안 스키마 생성
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer")
                .description("JWT 토큰을 입력하세요 (Bearer 접두사 제외)");
    }
}