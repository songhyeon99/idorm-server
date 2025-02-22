package idorm.idormServer.community.service;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.community.domain.PostPhoto;
import idorm.idormServer.community.repository.PostPhotoRepository;
import idorm.idormServer.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static idorm.idormServer.common.exception.ExceptionCode.POSTPHOTO_NOT_FOUND;
import static idorm.idormServer.common.exception.ExceptionCode.SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostPhotoService {

    @Value("${s3.bucket-name.post-photo}")
    private String postPhotoBucketName;
    private final PhotoService photoService;
    private final PostPhotoRepository postPhotoRepository;

    /**
     * DB에 PostPhoto 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void save(PostPhoto photo) {
        try {
            postPhotoRepository.save(photo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 게시글 사진 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(PostPhoto postPhoto) {
        try {
            postPhoto.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 커뮤니티 게시글 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public List<PostPhoto> savePostPhotos(Post post, List<MultipartFile> files) {

        String folderName = post.getDormCategory() + "/" + "post-" + post.getId();

        List<PostPhoto> savedPhotos = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");
            String photoUrl = photoService.insertFileToS3(postPhotoBucketName, folderName, fileName, file);

            PostPhoto savedPostPhoto = null;

            try {
                savedPostPhoto = PostPhoto.builder()
                        .post(post)
                        .photoUrl(photoUrl)
                        .build();
            } catch (RuntimeException e) {
                throw new CustomException(e, SERVER_ERROR);
            }
            save(savedPostPhoto);

            savedPhotos.add(savedPostPhoto);
        }
        return savedPhotos;
    }

    /**
     * 게시글로 전체 게시글 사진 조회 |
     * 500(SERVER_ERROR)
     */
    public List<PostPhoto> findAllByPost(Post post) {
        try {
            return postPhotoRepository.findAllByPostAndIsDeletedIsFalse(post);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 게시글 사진 단건 조회 |
     * 404(POSTPHOTO_NOT_FOUND)
     */
    public PostPhoto findById(Long postId, Long photoId) {
        return postPhotoRepository.findByIdAndPostIdAndIsDeletedIsFalse(photoId, postId)
                .orElseThrow(() -> {
                    throw new CustomException(null, POSTPHOTO_NOT_FOUND);
                });
    }
}
