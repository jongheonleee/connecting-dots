package com.example.demo.application.validator.user;

import com.example.demo.dto.user.UserFormDto;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class UserValidator implements Validator {

    // 각 필드별 정규표현식
    private static final Pattern REG_FOR_SPECIAL_CHAR = Pattern.compile("[\\{\\}\\[\\]\\/?.;,|)*~`!^\\-_+<>@#$%&\\\\=\\('\"\"]");
    private static final Pattern REG_FOR_BLANK = Pattern.compile("\\s");
    private static final Pattern REG_FOR_EMAIL = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$");
    private static final Pattern REG_FOR_ID = Pattern.compile("^[a-z0-9_]{6,20}$");
    private static final Pattern REG_FOR_PWD = Pattern.compile("^[a-z0-9@!#$%&*]{6,20}$");
    private static final Pattern REG_FOR_BIRTH = Pattern.compile("^(19|20)\\d{2}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$");

    private static final String DATE_FORMAT = "yyyy/MM/dd";



    @Override
    public boolean supports(Class<?> clazz) {
        return UserFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserFormDto userFormDto = (UserFormDto) target;
        checkBlank(errors);
        checkFields(userFormDto, errors);
        String[] parsedSns = parseSns(userFormDto.getSns());
        userFormDto.setParsedSns(parsedSns);

    }

    private void checkBlank(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pwd", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birth", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sns", "required");
    }

    private void checkFields(UserFormDto userFormDto, Errors errors) {
        if (!isValidId(userFormDto.getId())) {
            errors.rejectValue("id", "invalidId");
        }

        if (!isValidName(userFormDto.getName())) {
            errors.rejectValue("name", "invalid");
        }

        if (!isValidEmail(userFormDto.getEmail())) {
            errors.rejectValue("email", "invalidEmail");
        }

        if (!isValidPwd(userFormDto.getPwd())) {
            errors.rejectValue("pwd", "invalidPwd");
        }

        if (!isValidBirth(userFormDto.getBirth())) {
            errors.rejectValue("birth", "invalidBirth");
        }
    }

    // 이름 -> 특수문자나 공백이 포함 되어 있는지 확인, 2글자 이상
    private boolean isValidName(String name) {
        return name.length() >= 2
                && !REG_FOR_SPECIAL_CHAR.matcher(name).find()
                && !REG_FOR_BLANK.matcher(name).find();
    }

    // 이메일 -> 이메일 형식인지 확인
    private boolean isValidEmail(String email) {
        return REG_FOR_EMAIL.matcher(email).find();
    }

    // 아이디 -> 영문자, 숫자, 특수문자 조합, 6글자 이상 20글자 이하
    private boolean isValidId(String id) {
        return 6 <= id.length() && id.length() <= 20
                && !REG_FOR_SPECIAL_CHAR.matcher(id).find()
                && !REG_FOR_BLANK.matcher(id).find()
                && REG_FOR_ID.matcher(id).find();
    }

    // 비밀번호 -> 영문자, 숫자, 특수문자 조합, 8글자 이상 20글자 이하
    private boolean isValidPwd(String pwd) {
        log.info("-----------------------call validate for pwd-----------------------");
        log.info("pwd: {}", pwd);
        log.info("pwd.length(): {}", pwd.length());
        log.info("REG_FOR_SPECIAL_CHAR.matcher(pwd).find(): {}", REG_FOR_SPECIAL_CHAR.matcher(pwd).find());
        log.info("REG_FOR_BLANK.matcher(pwd).find(): {}", REG_FOR_BLANK.matcher(pwd).find());
        log.info("REG_FOR_PWD.matcher(pwd).find(): {}", REG_FOR_PWD.matcher(pwd).find());

        return 8 <= pwd.length() && pwd.length() <= 20
                && REG_FOR_SPECIAL_CHAR.matcher(pwd).find()
                && !REG_FOR_BLANK.matcher(pwd).find()
                && REG_FOR_PWD.matcher(pwd).find();
    }

    // 생일 -> 날짜 형식인지 확인(yyyy/MM/dd)
    private boolean isValidBirth(String birth) {
        if (!REG_FOR_BIRTH.matcher(birth).find()) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);

        try {
            sdf.parse(birth);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // sns 파싱 처리해서 문자열 배열로 주기
        // kakaotalk,facebook,instagram -> [kakaotalk, facebook, instagram]
    private String[] parseSns(String sns) {
        String[] parsedSns = sns.split(",");
        return parsedSns;
    }
}
