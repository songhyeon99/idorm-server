package idorm.idormServer.controller;

import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.domain.Member;
import idorm.idormServer.dto.MatchingInfoSaveRequestDto;
import idorm.idormServer.jwt.JwtTokenProvider;
import idorm.idormServer.service.MatchingInfoService;
import idorm.idormServer.service.MemberService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = "온보딩 정보 API")
public class MatchingInfoController {

    private final MatchingInfoService matchingInfoService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 온보딩(매칭)정보 저장
     */
    @PostMapping("/matchinginfo")
    @ApiOperation(value = "온보딩 정보 저장", notes = "최초로 온보딩 정보를 저장할 경우만 사용 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "온보딩 정보 저장 성공"),
            @ApiResponse(code = 401, message = "UnAuthorized")
    })
    public ResponseEntity<DefaultResponseDto<Object>> saveMatchingInfo(HttpServletRequest request2, @RequestBody @Valid MatchingInfoSaveRequestDto request) {

        if(request2 == null) {
            throw new AccessDeniedException("UnAuthorized");
        }

        long userPk = Long.parseLong(jwtTokenProvider.getUserPk(request2.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(userPk);

        if(member.getMatchingInfo() != null) // 등록된 매칭정보가 있다면
            throw new IllegalArgumentException("이미 등록된 매칭정보가 있습니다.");

        Long matchingInfoId = matchingInfoService.save(request, member);

        return ResponseEntity.status(200)
            .body(DefaultResponseDto.builder()
                    .responseCode("OK")
                    .responseMessage("온보딩 정보 저장 완료")
                    .data(matchingInfoId)
                    .build()
            );
    }

    /**
     * 온보딩(매칭) 정보 수정
     */

    /**
     * 온보딩(매칭) 정보 조회
     */

    /**
     * 온보딩(매칭) 정보 삭제
     */
    // TODO: Member 탈퇴 시 삭제 처리

}
