package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 댓글 아이디를 통해서 멤버가 작성한 댓글 반환
     */
    @Query(value = "SELECT * " +
            "FROM comment c " +
            "WHERE c.member_id = :memberId AND " +
            "c.is_visible = 1 " +
            "ORDER BY c.updated_at", nativeQuery = true)
    List<Comment> findCommentsByMemberId(@Param("memberId") Long memberId);
}