package idorm.idormServer.email.dto;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({EmailAuthRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.Email.class
})
@ApiModel(value = "Email 인증 요청")
public class EmailAuthRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "이메일", example = "test1@inu.ac.kr")
    @NotBlank(message = "이메일을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    @Email(message = "올바른 이메일 형식이 아닙니다.", groups = ValidationSequence.Email.class)
    private String email;

    public idorm.idormServer.email.domain.Email toEntity(String verificationCode) {

        return idorm.idormServer.email.domain.Email.builder()
                .email(this.email)
                .code(verificationCode)
                .build();
    }
}