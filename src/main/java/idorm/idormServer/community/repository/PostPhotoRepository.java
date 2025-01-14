package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {

    Optional<PostPhoto> findByIdAndPostIdAndIsDeletedIsFalse(Long id, Long postId);

    List<PostPhoto> findAllByPostAndIsDeletedIsFalse(Post post);
}
