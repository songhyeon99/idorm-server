package idorm.idormServer.matchingInfo.service;

import idorm.idormServer.exception.CustomException;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.dto.MatchingInfoDefaultRequestDto;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.matchingInfo.repository.MatchingInfoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingInfoService {

    private final MatchingInfoRepository matchingInfoRepository;

    /**
     * 기숙사 분류 체크
     */
    private void validateDormNum(String dormNum) {
        if(!dormNum.equals("DORM1") && !dormNum.equals("DORM2") && !dormNum.equals("DORM3")) {
            throw new CustomException(DORM_CATEGORY_FORMAT_INVALID);
        }
    }

    /**
     * 입사 기간 체크
     */
    private void validateJoinPeriod(String joinPeriod) {
        if(!joinPeriod.equals("WEEK16") &&
                !joinPeriod.equals("WEEK24")) {
            throw new CustomException(JOIN_PERIOD_FORMAT_INVALID);
        }
    }

    /**
     * MatchingInfo 저장 |
     */
    @Transactional
    public MatchingInfo save(MatchingInfoDefaultRequestDto requestDto, Member member) {

        validateDormNum(requestDto.getDormNum());
        validateJoinPeriod(requestDto.getJoinPeriod());

        MatchingInfo matchingInfo = requestDto.toEntity(member);
        matchingInfoRepository.save(matchingInfo);

        return matchingInfo;
    }

    /**
     * MatchingInfo 매칭이미지 공개여부 변경 |
     * true일 경우만 매칭 시 조회한다.
     */
    @Transactional
    public MatchingInfo updateMatchingInfoIsPublic(Member member, Boolean isMatchingInfoPublic) {

        MatchingInfo foundMatchingInfo = matchingInfoRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new CustomException(MATCHING_INFO_NOT_FOUND));

        try {
            foundMatchingInfo.updateIsMatchingInfoPublic(isMatchingInfoPublic);
            matchingInfoRepository.save(foundMatchingInfo);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
        return foundMatchingInfo;
    }

    /**
     * MatchingInfo 단건 조회 |
     * 매칭인포 식별자로 매칭인포를 단건 조회한다. 저장된 매칭정보가 없다면 404(Not Found)를 던진다.
     */
    public MatchingInfo
    findById(Long matchingInfoId) {

        MatchingInfo foundMatchingInfo = matchingInfoRepository.findById(matchingInfoId)
                .orElseThrow(() -> new CustomException(MATCHING_INFO_NOT_FOUND));
        return foundMatchingInfo;
    }

    /**
     * MatchingInfo Optional 단건 조회 |
     */
    public Optional<MatchingInfo> findOpByMemberId(Long memberId) {
        Optional<MatchingInfo> foundMatchingInfo = matchingInfoRepository.findByMemberId(memberId);

        return foundMatchingInfo;
    }

    /**
     * MatchingInfo 단건 조회 |
     * 멤버 식별자로 매칭인포를 단건 조회한다. 저장된 매칭정보가 없다면 404(Not Found)를 던진다.
     */
    public Long findByMemberId(Long memberId) {

        Long matchingInfoId = matchingInfoRepository.findMatchingInfoIdByMemberId(memberId)
                .orElseThrow(() -> new CustomException(MATCHING_INFO_NOT_FOUND));

        return matchingInfoId;
    }

    /**
     * MatchingInfo 삭제 |
     */
    @Transactional
    public void deleteMatchingInfo(MatchingInfo matchingInfo) {

        try {
            matchingInfoRepository.delete(matchingInfo);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }

    /**
     * MatchingInfo 수정 |
     */
    @Transactional
    public void updateMatchingInfo(Long matchingInfoId, MatchingInfoDefaultRequestDto requestDto) {

        MatchingInfo updateMatchingInfo = matchingInfoRepository.findById(matchingInfoId)
                .orElseThrow(() -> new CustomException(MATCHING_INFO_NOT_FOUND));

        validateDormNum(requestDto.getDormNum());
        validateJoinPeriod(requestDto.getJoinPeriod());

        try {
            updateMatchingInfo.updateMatchingInfo(requestDto);
            matchingInfoRepository.save(updateMatchingInfo);
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }
    }
}
