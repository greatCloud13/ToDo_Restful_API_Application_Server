package com.example.webapp.common.Aspect;

import com.example.jwt.Repository.UserRepository;
import com.example.webapp.common.annotations.InjectUserEntity;
import com.example.webapp.common.context.UserContext;
import com.example.webapp.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class UserEntityInjectionAspect {

    private final UserRepository userRepository;

    @Around("@annotation(InjectUserEntity)")
    public Object injectUserEntity(ProceedingJoinPoint joinPoint) throws Throwable{
        try {

            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "사용자를 찾을 수 없습니다"
                    ));

            UserContext.setCurrentUser(user);

        }finally {
            UserContext.clear();
        }

    }

}
