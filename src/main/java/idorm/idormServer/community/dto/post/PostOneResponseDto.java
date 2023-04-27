package idorm.idormServer.community.dto.post;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.dto.comment.CommentParentResponseDto;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.photo.domain.PostPhoto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "Post 단건 응답")
public class PostOneResponseDto {

    @ApiModelProperty(position = 1, value= "게시글 식별자", example = "1")
    private Long postId;

    @ApiModelProperty(position = 2, value= "멤버 식별자", example = "2")
    private Long memberId;

    @ApiModelProperty(position = 3, value = "기숙사 분류", example = "DORM1", allowableValues = "DORM1, DORM2, DORM3")
    private DormCategory dormCategory;

    @ApiModelProperty(position = 4, value = "게시글 제목", example = "제에목")
    private String title;

    @ApiModelProperty(position = 5, value = "게시글 내용", example = "내애용")
    private String content;

    @ApiModelProperty(position = 6, value = "닉네임", example = "null(탈퇴), 익명, 응철이")
    private String nickname;

    @ApiModelProperty(position = 7, value = "프로필사진 주소", example = "null(사진이 없거나, 익명), url")
    private String profileUrl;

    @ApiModelProperty(position = 8, value = "공감 수")
    private int likesCount;

    @ApiModelProperty(position = 9, value = "댓글 수")
    private int commentsCount;

    @ApiModelProperty(position = 10, value = "이미지 수")
    private int imagesCount;

    @ApiModelProperty(position = 11, value = "공감 여부", allowableValues = "true(공감), false(공감 안함), null(게시글 단건 " +
            "조회가 아닌 경우)")
    private Boolean isLiked;

    @ApiModelProperty(position = 12, value = "생성일자")
    private LocalDateTime createdAt;

    @ApiModelProperty(position = 13, value = "수정일자")
    private LocalDateTime updatedAt;

    @ApiModelProperty(position = 14, value = "익명여부")
    private Boolean isAnonymous;

    @ApiModelProperty(position = 15, value = "게시글 업로드 사진들")
    private List<PostPhotoDefaultResponseDto> postPhotos = new ArrayList<>();

    @ApiModelProperty(position = 16, value = "댓글/대댓글 목록")
    private List<CommentParentResponseDto> comments = new ArrayList<>();

    // 게시글 저장 시에만 사용
    public PostOneResponseDto(Post post) {
        this.postId = post.getId();
        this.memberId = post.getMember().getId();
        this.dormCategory = DormCategory.valueOf(post.getDormCategory());
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isLiked = false;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.likesCount = 0;
        this.commentsCount = 0;
        this.imagesCount = 0;
        this.isAnonymous = post.getIsAnonymous();

        if(post.getMember().getIsDeleted()) { // 회원 탈퇴의 경우
            this.nickname = null;
            this.memberId = null;
        } else if(!post.getIsAnonymous()) { // 익명이 아닌 경우
            this.nickname = post.getMember().getNickname();

            if(post.getMember().getMemberPhoto() != null)
                this.profileUrl = post.getMember().getMemberPhoto().getPhotoUrl();
        } else if(post.getIsAnonymous()) { // 익명일 경우
            this.nickname = "익명";
        }

        if(post.getPostPhotosIsDeletedIsFalse() != null) {
            List<PostPhoto> postPhotos = post.getPostPhotosIsDeletedIsFalse();
            this.imagesCount = postPhotos.size();

            for (PostPhoto postPhoto : postPhotos)
                this.postPhotos.add(new PostPhotoDefaultResponseDto(postPhoto));
        }
    }

    public PostOneResponseDto(Post post, List<CommentParentResponseDto> comments, boolean isLiked) {
        this.postId = post.getId();
        this.memberId = post.getMember().getId();
        this.dormCategory = DormCategory.valueOf(post.getDormCategory());
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isLiked = isLiked;
        this.likesCount = 0;
        this.commentsCount = 0;
        this.imagesCount = 0;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.isAnonymous = post.getIsAnonymous();

        if (post.getPostLikedMembersIsDeletedIsFalse() != null)
            this.likesCount = post.getPostLikedMembersCnt();

        if (post.getCommentsIsDeletedIsFalse() != null)
            this.commentsCount = post.getCommentsCount();

        if(post.getMember().getIsDeleted()) { // 회원 탈퇴의 경우
            this.memberId = null;
            this.nickname = null;
        } else if(!post.getIsAnonymous()) { // 익명이 아닌 경우
            this.nickname = post.getMember().getNickname();
            if(post.getMember().getMemberPhoto() != null) {
                this.profileUrl = post.getMember().getMemberPhoto().getPhotoUrl();
            }
        } else if(post.getIsAnonymous()) { // 익명일 경우
            this.nickname = "익명";
        }

        if(post.getPostPhotosIsDeletedIsFalse() != null) {
            List<PostPhoto> postPhotos = post.getPostPhotosIsDeletedIsFalse();
            this.imagesCount = postPhotos.size();

            for (PostPhoto postPhoto : postPhotos)
                this.postPhotos.add(new PostPhotoDefaultResponseDto(postPhoto));
        }

        if(comments != null) {
            for(CommentParentResponseDto comment : comments) {
                this.comments.add(comment);
            }
        }
    }
}