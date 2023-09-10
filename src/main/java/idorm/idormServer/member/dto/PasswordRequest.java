package idorm.idormServer.member.dto;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({PasswordRequest.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.Email.class,
        ValidationSequence.Size.class,
        ValidationSequence.Pattern.class,
})
@Schema(title = "Member 비밀번호 변경 요청")
public class PasswordRequest {

    @Schema(required = true, description = "이메일", example = "test1@inu.ac.kr")
    @NotBlank(message = "이메일 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Email(message = "올바른 형식의 이메일이 아닙니다.", groups = ValidationSequence.Email.class)
    private String email;

    @Schema(required = true, description = "비밀번호", example = "aaaaaaa7!")
    @NotBlank(message = "비밀번호 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,15}$",
            message = "비밀번호는 영소문자, 숫자, 특수문자는 필수이고 대문자는 선택인 형식만 가능합니다.",
            groups = ValidationSequence.Pattern.class)
    private String password;
}