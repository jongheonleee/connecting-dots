package com.example.demo.validator;

import com.example.demo.dto.BoardFormDto;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BoardValidator implements Validator {

    private static final Pattern REG_NUMERIC_CHAR = Pattern.compile("^\\d+$");

    @Override
    public boolean supports(Class<?> clazz) {
        return BoardFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BoardFormDto boardFormDto = (BoardFormDto) target;
        checkBlank(errors);
        checkFields(boardFormDto, errors);
    }

    private void checkBlank(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cate_code", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "required");

    }

    private void checkFields(BoardFormDto boardFormDto, Errors errors) {
        if (!isValidCategory(boardFormDto.getCate_code())) {
            errors.rejectValue("cate_code", "invalidCateCode");
        }

        if (!isValidTitle(boardFormDto.getTitle())) {
            errors.rejectValue("title", "invalidTitle");
        }

        if (!isValidContent(boardFormDto.getContent())) {
            errors.rejectValue("content", "invalidContent");
        }
    }

    private boolean isValidCategory(String cate_code) {
        return cate_code.length() == 4 &&
                REG_NUMERIC_CHAR.matcher(cate_code).matches();
    }

    private boolean isValidTitle(String title) {
        return 1 <= title.length() && title.length() <= 50;
    }

    private boolean isValidContent(String content) {
        return 1 <= content.length() && content.length() <= 2_000;
    }
}
