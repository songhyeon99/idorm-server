package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.PostLikedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostLikedMemberRepository extends JpaRepository<PostLikedMember, Long> {

    @Query(value = "SELECT post_id " +
            "FROM post_liked_member plm " +
            "WHERE plm.member_id = :memberId", nativeQuery = true)
    List<Long> findLikedPostsByMemberId(@Param("memberId") Long memberId);

    @Query(value = "SELECT member_id " +
            "FROM post_liked_member plm " +
            "WHERE plm.post_id = :postId", nativeQuery = true)
    List<Long> findLikedMemberByPostId(@Param("postId") Long postId);

    @Query(value = "SELECT * " +
            "FROM post_liked_member plm " +
            "WHERE plm.post_id = :postId AND " +
            "plm.member_id = :memberId", nativeQuery = true)
    Optional<PostLikedMember> findOneByMemberIdAndPostId(@Param("postId") Long postId,
                                                         @Param("memberId") Long memberId);
}
