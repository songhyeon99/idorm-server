package idorm.idormServer.member.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.service.EmailService;
import idorm.idormServer.exception.CustomException;

import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.dto.*;
import idorm.idormServer.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.exception.ExceptionCode.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Api(tags = "회원")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final EmailService emailService;
//    private final LikedMemberService likedMemberService;
//    private final DislikedMemberService dislikedMemberService;

    @Value("${DB_USERNAME}")
    private String ENV_USERNAME;

    @Value("${ADMIN_PASSWORD}")
    private String ENV_PASSWORD;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "회원 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MEMBER_FOUND",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    })
    @GetMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> findOneMember(
            HttpServletRequest request
    ) {
        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(loginMemberId);

        MemberDefaultResponseDto response = new MemberDefaultResponseDto(member);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_FOUND")
                        .responseMessage("Member 단건 조회 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "회원 가입", notes = "회원 가입은 이메일 인증이 완료된 후 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "MEMBER_REGISTERED",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_EMAIL"),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND"),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_EMAIL / DUPLICATE_NICKNAME"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<DefaultResponseDto<Object>> saveMember(
            @RequestBody @Valid MemberSaveRequestDto request
    ) {

        Email foundEmail = emailService.findByEmail(request.getEmail());

        if (!foundEmail.isCheck()) {
            throw new CustomException(UNAUTHORIZED_EMAIL);
        }

        memberService.isExistingEmail(request.getEmail());
        memberService.isExistingNickname(request.getNickname());

        Member createdMember = memberService.createMember(request);

        emailService.updateIsJoined(foundEmail);

        MemberDefaultResponseDto response = new MemberDefaultResponseDto(createdMember);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_REGISTERED")
                        .responseMessage("Member 회원가입 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "프로필 사진 저장")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "PROFILE_PHOTO_SAVED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "FILE_NOT_FOUND"),
            @ApiResponse(responseCode = "413",
                    description = "FILE_SIZE_EXCEED"),
            @ApiResponse(responseCode = "415",
                    description = "FILE_TYPE_UNSUPPORTED"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/member/profile-photo")
    public ResponseEntity<DefaultResponseDto<Object>> saveMemberProfilePhoto(
            HttpServletRequest request, @RequestPart(value = "file", required = false) MultipartFile file) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        if(file == null) {
            throw new CustomException(FILE_NOT_FOUND);
        }

        memberService.saveProfilePhoto(loginMember, file);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("PROFILE_PHOTO_SAVED")
                        .responseMessage("Member 프로필 사진 저장 완료")
                        .build());
    }

    @ApiOperation(value = "프로필 사진 삭제", notes = "삭제할 사진이 없다면 404(FILE_NOT_FOUND)를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "PROFILE_PHOTO_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "404",
                    description = "FILE_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    }
    )
    @DeleteMapping("/member/profile-photo")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMemberProfilePhoto(
            HttpServletRequest request) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        memberService.deleteMemberProfilePhoto(loginMember);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("PROFILE_PHOTO_DELETED")
                        .responseMessage("Member 프로필 사진 삭제 완료")
                        .build());
    }

    @ApiOperation(value = "비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "PASSWORD_UPDATED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / *_CHARACTER_INVALID / *_LENGTH_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_EMAIL"),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND / MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    }
    )
    @PatchMapping("/password")
    public ResponseEntity<DefaultResponseDto<Object>> updatePassword(
            @RequestBody @Valid MemberUpdatePasswordRequestDto request) {

        Email email = emailService.findByEmail(request.getEmail());

        if (!email.isCheck()) {
            throw new CustomException(UNAUTHORIZED_EMAIL);
        }
        
        Member foundMember = memberService.findByEmail(request.getEmail());
        memberService.updatePassword(foundMember, passwordEncoder.encode(request.getPassword()));

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("PASSWORD_UPDATED")
                        .responseMessage("Member 비밀번호 변경 완료")
                        .build());

    }

    @ApiOperation(value = "닉네임 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "NICKNAME_UPDATED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / NICKNAME_CHARACTER_INVALID / NICKNAME_LENGTH_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_SAME_NICKNAME / DUPLICATE_NICKNAME / CANNOT_UPDATE_NICKNAME"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    }
    )
    @PatchMapping("/member/nickname")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberNickname(
            HttpServletRequest request2,
            @RequestBody @Valid MemberUpdateNicknameRequestDto request) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request2.getHeader("X-AUTH-TOKEN")));
        Member loginMember = memberService.findById(loginMemberId);

        memberService.updateNickname(loginMember, request.getNickname());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("NICKNAME_UPDATED")
                        .responseMessage("Member 닉네임 변경 완료")
                        .build());
    }

    @ApiOperation(value = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MEMBER_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    }
    )
    @DeleteMapping("/member")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMember(
            HttpServletRequest request
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(request.getHeader("X-AUTH-TOKEN")));
        Member foundMember = memberService.findById(loginMemberId);

//        likedMemberService.deleteLikedMembers(foundMember.getId());
//        dislikedMemberService.deleteDislikedMembers(foundMember.getId());
        memberService.deleteMember(foundMember);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_DELETED")
                        .responseMessage("Member 삭제 완료")
                        .build());
    }


    @ApiOperation(value = "로그인", notes = "헤더에 토큰을 담아 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "MEMBER_LOGIN",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "FIELD_REQUIRED / EMAIL_CHARACTER_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_PASSWORD"),
            @ApiResponse(responseCode = "404",
                    description = "EMAIL_NOT_FOUND / MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    }
    )
    @PostMapping("/login")
    public ResponseEntity<DefaultResponseDto<Object>> login(
            @RequestBody @Valid MemberLoginRequestDto request) {

        Member loginMember = null;

        if(request.getEmail().equals(ENV_USERNAME + "@inu.ac.kr")) {
            if (!passwordEncoder.matches(request.getPassword(), passwordEncoder.encode(ENV_PASSWORD))) {
                throw new CustomException(UNAUTHORIZED_PASSWORD);
            }
            loginMember = memberService.findById(1L);
        } else {
            loginMember = memberService.findByEmail(request.getEmail());

            if (!passwordEncoder.matches(request.getPassword(), loginMember.getPassword())) {
                throw new CustomException(UNAUTHORIZED_PASSWORD);
            }
        }

        Iterator<String> iter = loginMember.getRoles().iterator();
        List<String> roles = new ArrayList<>();

        while (iter.hasNext()) {
            roles.add(iter.next());
        }

        String token = jwtTokenProvider.createToken(loginMember.getUsername(), roles);
        MemberDefaultResponseDto response = new MemberDefaultResponseDto(loginMember);

        return ResponseEntity.status(200)
                .header(AUTHORIZATION, token)
                .body(DefaultResponseDto.builder()
                        .responseCode("MEMBER_LOGIN")
                        .responseMessage("회원 로그인 완료")
                        .data(response)
                        .build());
    }

    /**
     * admin role
     */

    @ApiOperation(value = "관리자용 / Member 전체 조회", notes = "가입된 전체 멤버에 대한 데이터를 조회할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    }
    )
    @GetMapping("/admin/members")
    public ResponseEntity<DefaultResponseDto<Object>> members() {

        List<Member> members = memberService.findAll();
        List<MemberDefaultResponseDto> collect = members.stream()
                .map(o -> new MemberDefaultResponseDto(o)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 전체 조회 완료")
                        .data(new Result(collect))
                        .build());
    }

    @ApiOperation(value = "관리자용 / 특정 Member 정보 수정 (비밀번호, 닉네임)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "PASSWORD_CHARACTER_INVALID / NICKNAME_CHARACTER_INVALID / FIELD_REQUIRED " +
                            "/ UPDATEMEMBERID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "409",
                    description = "DUPLICATE_NICKNAME"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    }
    )
    @PatchMapping("/admin/member/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> updateMemberRoot(
            @PathVariable("id") @Positive(message = "회원 식별자는 양수만 가능합니다.") Long updateMemberId,
            @RequestBody @Valid MemberUpdateStatusAdminRequestDto request) {

        Member updateMember = memberService.findById(updateMemberId);

        memberService.updatePassword(updateMember, request.getPassword());
        memberService.updateNicknameByAdmin(updateMember, request.getNickname());

        MemberDefaultResponseDto response = new MemberDefaultResponseDto(updateMember);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 정보 수정 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "관리자용 / 특정 Member 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MemberDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "INTERNAL_SERVER_ERROR"),
    }
    )
    @DeleteMapping("/admin/member/{id}")
    public ResponseEntity<DefaultResponseDto<Object>> deleteMemberRoot(
            @PathVariable("id") Long deleteMemberId
    ) {
        Member foundMember = memberService.findById(deleteMemberId);
//        likedMemberService.deleteLikedMembers(foundMember.getId());
//        dislikedMemberService.deleteDislikedMembers(foundMember.getId());
        memberService.deleteMember(foundMember);

        List<Member> members = memberService.findAll();
        List<MemberDefaultResponseDto> collect = members.stream()
                .map(o -> new MemberDefaultResponseDto(o)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OK")
                        .responseMessage("Member 삭제 완료")
                        .data(new Result(collect))
                        .build());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
