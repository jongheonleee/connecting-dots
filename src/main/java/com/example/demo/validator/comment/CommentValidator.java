package com.example.demo.validator.comment;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.example.demo.dto.comment.CommentRequestDto;


@Component
public class CommentValidator implements Validator {

    private static final Pattern BAD_WORDS_REGEX = Pattern.compile("(ㅅㅂ|ㅂㅅ|ㄴㅇㅁ|씨발|ㅈㄴ|존나|개새끼|미친놈|ㅄ|ㅁㅊ|병신|멍청이|염병|죽일놈|좆|꺼져|죽어)");

    private static final Pattern BLANK_REGEX = Pattern.compile("^\\s+$");

    @Override
    public boolean supports(Class<?> clazz) {
        return CommentRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CommentRequestDto commentRequestDto = (CommentRequestDto) target;
        checkBlank(errors);
        checkFields(commentRequestDto, errors);
    }

    private void checkBlank(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bno", "required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "comment", "required");
    }

    private void checkFields(CommentRequestDto commentRequestDto, Errors errors) {
        if (isBadWordsComment(commentRequestDto.getComment())) {
            errors.rejectValue("comment", "invalidBadWordsComment");
        }

        if (!isValidComment(commentRequestDto.getComment())) {
            errors.rejectValue("comment", "invalidComment");
        }

        if (isBlank(commentRequestDto.getComment())) {
            errors.rejectValue("comment", "invalidBlank");
        }
    }

    private boolean isBadWordsComment(String comment) {
        return BAD_WORDS_REGEX.matcher(comment)
                              .find();
    }

    private boolean isValidComment(String comment) {
        return 1 <= comment.length() && comment.length() <= 500;
    }

    private boolean isBlank(String comment) {
        return BLANK_REGEX.matcher(comment)
                          .matches();
    }

}
