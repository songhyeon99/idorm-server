package idorm.idormServer.member.dto;

import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@ApiModel(value = "Member 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberDefaultResponseDto {

    @ApiModelProperty(position = 1, required = true, dataType = "Long", value = "멤버 식별자", example = "1")
    private Long id;

    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "이메일", example = "aaa@inu.ac.kr")
    private String email;

    @ApiModelProperty(position = 3, dataType = "String", value = "닉네임", example = "애교쟁이응철이")
    private String nickname;

    @ApiModelProperty(position = 5, dataType = "String", value = "프로필사진 주소", example = "aws S3 저장 url")
    private String profilePhotoUrl;

    @ApiModelProperty(position = 6, dataType = "Long", value = "매칭정보 식별자", example = "3")
    private Long matchingInfoId;

    @ApiModelProperty(position = 7, dataType = "String", value = "로그인 토큰")
    private String loginToken;

    public MemberDefaultResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();

        if(member.getMatchingInfo() != null) {
            this.matchingInfoId = member.getMatchingInfo().getId();
        }

        if(member.getPhoto() != null) {
            this.profilePhotoUrl = member.getPhoto().getUrl();
        }
    }

    public MemberDefaultResponseDto(Member member, String token) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.loginToken = token;

        if(member.getPhoto() != null) {
            this.profilePhotoUrl = member.getPhoto().getUrl();
        }

        if(member.getMatchingInfo() != null) {
            this.matchingInfoId = member.getMatchingInfo().getId();
        }

    }
}