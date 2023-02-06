package idorm.idormServer.matching.service;

import idorm.idormServer.exception.CustomException;

import idorm.idormServer.matching.dto.MatchingFilteredMatchingInfoRequestDto;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.repository.MatchingInfoRepository;
import idorm.idormServer.matchingInfo.service.MatchingInfoService;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingInfoRepository matchingInfoRepository;
    private final MemberService memberService;
    private final MatchingInfoService matchingInfoService;
    private final DislikedMemberService dislikedMemberService;


    /**
     * Matching 매칭멤버 전체 조회 |
     * MatchingInfo를 통해 조건에 맞는 멤버들로 필터링 후, 싫어요한 멤버를 필터링합니다.
     * 필터링된 MatchingInfo 식별자를 반환합니다.
     */
    public List<Long> findMatchingMembers(Long memberId) {

        Member loginMember = memberService.findById(memberId);

        if (loginMember.getMatchingInfo() == null) {
            throw new CustomException(MATCHING_INFO_NOT_FOUND);
        }

        if (loginMember.getMatchingInfo().getIsMatchingInfoPublic() == false) {
            throw new CustomException(ILLEGAL_STATEMENT_MATCHING_INFO_NON_PUBLIC);
        }

        List<Long> filteredMatchingInfoId = new ArrayList<>();

        try {
            MatchingInfo loginMemberMatchingInfo = loginMember.getMatchingInfo();

            filteredMatchingInfoId = matchingInfoRepository.findMatchingMembers(
                    memberId,
                    loginMemberMatchingInfo.getDormCategory(),
                    loginMemberMatchingInfo.getJoinPeriod(),
                    loginMemberMatchingInfo.getGender()
            );
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }

        List<Long> dislikedMembersId = dislikedMemberService.findDislikedMembers(memberId);

        Iterator<Long> iterator = filteredMatchingInfoId.iterator();
        while (iterator.hasNext()) {
            Long matchingInfoId = iterator.next();
            Long filteredMemberId = matchingInfoService.findById(matchingInfoId).getMember().getId();

            for (Long dislikedMemberId : dislikedMembersId) {
                if (filteredMemberId == dislikedMemberId) {
                    iterator.remove();
                }
            }
        }
        return filteredMatchingInfoId;
    }

    /**
     * Matching 매칭멤버 필터링 조회 |
     */
    public List<Long> findFilteredMatchingMembers(Long memberId,
                                                  MatchingFilteredMatchingInfoRequestDto filteringRequest) {

        Member loginMember = memberService.findById(memberId);

        if(loginMember.getMatchingInfo() == null) {
            throw new CustomException(MATCHING_INFO_NOT_FOUND);
        }

        if(loginMember.getMatchingInfo().getIsMatchingInfoPublic() == false) {
            throw new CustomException(ILLEGAL_STATEMENT_MATCHING_INFO_NON_PUBLIC);
        }

        int isSnoring = (filteringRequest.isSnoring() == true) ? 1 : 0; // true:1 , false: 0 / false 무조건 조회
        int isSmoking = (filteringRequest.isSmoking() == true) ? 1 : 0; // false 무조건 조회
        int isGrinding = (filteringRequest.isGrinding() == true) ? 1 : 0; // false 무조건 조회
        int isWearEarphones = (filteringRequest.isWearEarphones() == true) ? 1 : 0; // true 이면 무조건 조회
        int isAllowedFood = (filteringRequest.isAllowedFood() == true) ? 1 : 0; // false 이면 무조건 조회

        List<Long> filteredMatchingInfoId = new ArrayList<>();

        try {
            filteredMatchingInfoId = matchingInfoRepository.findFilteredMatchingMembers(
                    memberId,
                    filteringRequest.getDormCategory().getType(),
                    filteringRequest.getJoinPeriod().getType(),
                    loginMember.getMatchingInfo().getGender(),
                    isSnoring,
                    isSmoking,
                    isGrinding,
                    isWearEarphones,
                    isAllowedFood,
                    filteringRequest.getMinAge(),
                    filteringRequest.getMaxAge()
            );
        } catch (RuntimeException e) {
            throw new CustomException(SERVER_ERROR);
        }

        List<Long> dislikedMembersId = dislikedMemberService.findDislikedMembers(memberId);

        Iterator<Long> iterator = filteredMatchingInfoId.iterator();
        while (iterator.hasNext()) {
            Long matchingInfoId = iterator.next();
            Long filteredMemberId = matchingInfoService.findById(matchingInfoId).getMember().getId();

            for (Long dislikedMemberId : dislikedMembersId) {
                if (filteredMemberId == dislikedMemberId) {
                    iterator.remove();
                }
            }
        }
        return filteredMatchingInfoId;
    }
}
