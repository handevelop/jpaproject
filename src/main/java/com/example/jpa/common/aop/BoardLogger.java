package com.example.jpa.common.aop;

import com.example.jpa.logs.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Aspect
@Component
@Slf4j
public class BoardLogger {

    private final LogService logService;

    // com.example.jpa 로 시작하는 패키지에서 Controller 로 끝나는 패키지의 detail 메소드
    @Around("execution(* com.example.jpa..*.*Controller.detail(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("#########");
        log.info("컨트롤러 호출 전");
        log.info("#########");

        Object result = joinPoint.proceed();

        if (joinPoint.getSignature().getDeclaringTypeName().contains("ApiBoardController")
            && "detail".equals(joinPoint.getSignature().getName())) {

            StringBuilder sb = new StringBuilder();

            sb.append("\n");
            sb.append("함수위치 : " + joinPoint.getSignature().getDeclaringTypeName());
            sb.append("\n");
            sb.append("함수명 : " + joinPoint.getSignature().getName());
            sb.append("\n");

            Object[] args = joinPoint.getArgs();
            Arrays.stream(args).forEach(e -> sb.append(e.toString()));

            sb.append(result.toString());

            log.info(sb.toString());

            logService.add(sb.toString());
        }


        log.info("#########");
        log.info("컨트롤러 호출 후");
        log.info("#########");

        return result;
    }
}
