package com.example.webapp.common.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)     // 메서드에 사용 가능
@Retention(RetentionPolicy.RUNTIME) // 어노테이션 정보를 언제까지 유지할 지
@Documented
public @interface InjectUserEntity {

    /**
     * User를 찾지 못했을 때 예외를 발생시키는지 여부
     * @return true면 예외 발생, false면 null 허용
     */
    boolean required() default true;
}
