package com.example.demo.aspect;

import com.example.demo.annotation.PasswordEncryption;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PasswordEncryptionAspect {

    private final PasswordEncoder passwordEncoder;

    // 암호화 처리
    @Around("execution(* com.example.demo.controller..*.*(..))") // 암호화 처리 대상 패키지
    public Object passwordEncryption(ProceedingJoinPoint pjp) throws Throwable {
        Arrays.stream(pjp.getArgs()).forEach(this::fieldEncryption);// 암호화 처리 코드
        return pjp.proceed(); // 실제 코드
    }

    // 오브젝트 내에서 필드 중 암호화 대상 필드 찾아서 암호화 처리
    public void fieldEncryption(Object object) {
        // 오브젝트 empty면 호출 중단
        if (ObjectUtils.isEmpty(object)) {
            return;
        }

        // 필드별 암호화 대상 필드 탐색 -> 대상 필드 암호화 처리
        FieldUtils.getAllFieldsList(object.getClass()) // 클래스를 대상으로 필드 리스트 추출
                  .stream()
                  .filter(field -> !(Modifier.isFinal(field.getModifiers()) && !(Modifier.isStatic(field.getModifiers())))) // final, static 필드 제외
                  .forEach(field -> {
                      try {
                          boolean encryptionTarget = field.isAnnotationPresent(PasswordEncryption.class);// 암호화 어노테이션 체크
                          // 암호화 대상 필드가 아닌 경우 호출 중단
                          if (!encryptionTarget) {
                              return;
                          }

                          // 필드가 String 타입이 아닌 경우 호출 중단
                          Object encryptionField = FieldUtils.readField(field, object, true);
                          if (!(encryptionField instanceof String)) {
                              return;
                          }

                          // 암호화 처리
                          String encryptedPassword = passwordEncoder.encode((String) encryptionField);
                          FieldUtils.writeField(field, object, encryptedPassword, true);
                      } catch (Exception e) {
                          log.error("암호화 처리 중 에러 발생", e);
                          throw new RuntimeException(e);
                      }
                  });
    }


}
