package com.example.demo.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BoardChangeHistoryRequest {

    @NotNull(message = "bno 필수값입니다.")
    private Integer bno;

    @NotBlank(message = "title 필수값입니다.")
    private String title;

    @NotBlank(message = "cont 필수값입니다.")
    private String cont;

    private String comt;
}
