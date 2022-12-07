package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "MatchingInfo 응답")
@AllArgsConstructor
public class MatchingInfoDefaultResponseDto {

    @ApiModelProperty(position = 1, value="식별자", example = "2")
    private Long id;

    @ApiModelProperty(position = 2, value = "기숙사 분류", example = "DORM1, DORM2, DORM3")
    private String dormNum;

    @ApiModelProperty(position = 3, value = "입사 기간", example = "WEEK16, WEEK24")
    private String joinPeriod;

    @ApiModelProperty(position = 4, value = "성별", example = "FEMALE, MALE")
    private String gender;

    @ApiModelProperty(position = 5, value = "나이", example = "20")
    private Integer age;

    @ApiModelProperty(position = 6, value = "코골이 여부", example = "false")
    private Boolean isSnoring;

    @ApiModelProperty(position = 7, value = "이갈이 여부", example = "false")
    private Boolean isGrinding;

    @ApiModelProperty(position = 8, value = "흡연 여부", example = "false")
    private Boolean isSmoking;

    @ApiModelProperty(position = 9, value = "실내 음식 허용 여부", example = "false")
    private Boolean isAllowedFood;

    @ApiModelProperty(position = 10, value = "이어폰 착용 여부", example = "false")
    private Boolean isWearEarphones;

    @ApiModelProperty(position = 11, value = "기상 시간", example = "아침 7시 기상~~")
    private String wakeUpTime;

    @ApiModelProperty(position = 12, value = "정리 정돈 상태", example = "깨끗해요~~")
    private String cleanUpStatus;

    @ApiModelProperty(position = 13, value = "샤워 시간", example = "7시에 씻어요~~")
    private String showerTime;

    @ApiModelProperty(position = 14, value = "오픈 채팅 링크", example = "링크~~")
    private String openKakaoLink;

    @ApiModelProperty(position = 15, value = "mbti", example = "ESTJ")
    private String mbti;

    @ApiModelProperty(position = 16, value = "룸메에게 하고싶은 말", example = "혜원이랑 룸메 하고 싶어요 ㅎ_ㅎ")
    private String wishText;

    @ApiModelProperty(position = 17, value = "매칭이미지 공개 여부", example = "true")
    private Boolean isMatchingInfoPublic;

    @ApiModelProperty(position = 18, value = "멤버 이메일", example = "aaa@inu.ac.kr")
    private String memberEmail;


    public MatchingInfoDefaultResponseDto(MatchingInfo matchingInfo) {

        this.id = matchingInfo.getId();
        this.dormNum = matchingInfo.getDormNum();
        this.joinPeriod = matchingInfo.getJoinPeriod();
        this.gender = matchingInfo.getGender();
        this.age = matchingInfo.getAge();
        this.isSnoring = matchingInfo.getIsSnoring();
        this.isGrinding = matchingInfo.getIsGrinding();
        this.isSmoking = matchingInfo.getIsSmoking();
        this.isAllowedFood = matchingInfo.getIsAllowedFood();
        this.isWearEarphones = matchingInfo.getIsWearEarphones();
        this.wakeUpTime = matchingInfo.getWakeUpTime();
        this.cleanUpStatus = matchingInfo.getCleanUpStatus();
        this.showerTime = matchingInfo.getShowerTime();
        this.openKakaoLink = matchingInfo.getOpenKakaoLink();
        this.mbti = matchingInfo.getMbti();
        this.wishText = matchingInfo.getWishText();
        this.isMatchingInfoPublic = matchingInfo.getIsMatchingInfoPublic();
        this.memberEmail = matchingInfo.getMember().getEmail();
    }

}
