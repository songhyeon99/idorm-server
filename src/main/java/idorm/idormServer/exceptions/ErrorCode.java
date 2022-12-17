package idorm.idormServer.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 400 BAD_REQUEST : 잘못된 요청
     */

    FIELD_REQUIRED(BAD_REQUEST, "입력은 필수 입니다."),
    FILE_SIZE_EXCEEDED(BAD_REQUEST, "파일 사이즈 초과 입니다."),

    EMAIL_FORMAT_INVALID(BAD_REQUEST, "올바른 형식의 이메일이 아닙니다."),
    PASSWORD_FORMAT_INVALID(BAD_REQUEST, "올바른 형식의 비밀번호가 아닙니다."),
    NICKNAME_FORMAT_INVALID(BAD_REQUEST, "올바른 형식의 닉네임이 아닙니다."),
    DORM_CATEGORY_FORMAT_INVALID(BAD_REQUEST, "올바른 형식의 기숙사 분류가 아닙니다."),
    JOIN_PERIOD_FORMAT_INVALID(BAD_REQUEST, "올바른 형식의 입사 기간이 아닙니다."),
    
    ILLEGAL_ARGUMENT_ADMIN(BAD_REQUEST, "관리자는 해당 요청의 설정 대상이 될 수 없습니다."),
    ILLEGAL_ARGUMENT_SELF(BAD_REQUEST, "본인은 해당 요청의 설정 대상이 될 수 없습니다."),
    ILLEGAL_ARGUMENT_SAME_PK(BAD_REQUEST, "부모 식별자와 자식 식별자가 같을 수 없습니다."),

    ILLEGAL_STATEMENT_MATCHING_INFO_NON_PUBLIC(BAD_REQUEST, "매칭정보가 비공개 입니다."),

    /**
     * 401 UNAUTHORIZED : 인증되지 않은 사용자
     */

    INVALID_CODE(UNAUTHORIZED, "올바르지 않은 코드 입니다."),
    EXPIRED_CODE(UNAUTHORIZED, "이메일 인증 유효시간이 초과되었습니다."),

    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "로그인이 필요합니다."),
    UNAUTHORIZED_DELETE(UNAUTHORIZED, "삭제 권한이 없습니다."),
    UNAUTHORIZED_PASSWORD(UNAUTHORIZED, "올바르지 않은 비밀번호 입니다."),

    /**
     * 403 FORBIDDEN : 권한이 없는 사용자
     */
    FORBIDDEN_AUTHORIZATION(FORBIDDEN, "접근 권한이 없습니다."),

    /**
     * 404 NOT_FOUND : Resource 를 찾을 수 없음
     */

    EMAIL_NOT_FOUND(NOT_FOUND, "등록된 이메일이 없습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "등록된 멤버가 없습니다."),
    FILE_NOT_FOUND(NOT_FOUND, "등록된 파일이 없습니다."),
    MATCHING_INFO_NOT_FOUND(NOT_FOUND, "등록된 매칭정보가 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "등록된 댓글이 없습니다."),
    POST_LIKED_MEMBER_NOT_FOUND(NOT_FOUND, "멤버가 게시글에 공감하지 않았습니다."),
    POST_NOT_FOUND(NOT_FOUND, "등록된 게시글이 없습니다."),
    LIKED_NOT_FOUND(NOT_FOUND, "등록된 공감이 없습니다."),
    CALENDAR_NOT_FOUND(NOT_FOUND, "등록된 캘린더 정보가 없습니다."),

    /**
     * 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재
     */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다."),

    DUPLICATE_EMAIL(CONFLICT, "이미 등록된 이메일 입니다."),
    DUPLICATE_MEMBER(CONFLICT, "이미 등록된 멤버 입니다."),
    DUPLICATE_NICKNAME(CONFLICT, "이미 등록된 닉네임 입니다."),
    DUPLICATE_SAME_NICKNAME(CONFLICT, "기존의 닉네임과 같습니다."),
    DUPLICATE_MATCHING_INFO(CONFLICT, "매칭정보가 이미 등록되어 있습니다."),
    DUPLICATE_LIKED_MEMBER(CONFLICT, "이미 좋아요한 멤버 입니다."),
    DUPLICATE_DISLIKED_MEMBER(CONFLICT, "이미 싫어요한 멤버 입니다."),
    DUPLICATE_LIKED(CONFLICT, "공감은 한 번만 가능합니다."),

    CANNOT_UPDATE_NICKNAME(CONFLICT, "닉네임은 30일마다 변경할 수 있습니다."),
    CANNOT_LIKED_SELF(CONFLICT, "본인의 글에 공감할 수 없습니다."),

    /**
     * 500 INTERNAL_SERVER_ERROR : 서버 에러
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다."),
    USERNAME_NOT_FOUND(INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String detail;
}
