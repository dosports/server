package umc.dosports.Like;

import umc.dosports.Like.model.GetReviewRes;
import umc.dosports.Like.model.PostLikeReq;

import java.util.List;

public interface LikeRepository {
    //좋아요
    int createLike(PostLikeReq likeReq);
    int increaseLike(PostLikeReq likeReq); //review table like+1
    void notifyLike(PostLikeReq likeReq, long likeIdx); //notify like

    //좋아요 취소
    void deleteLike(PostLikeReq likeReq, long userIdxByJWT);

    //좋아요 체크
    public boolean checkLike(long reviewIdx, long userIdxByJWT);

    //좋아요 리뷰 목록
    public List<GetReviewRes> getLikeReview(long userIdxByJWT, int pageNum);
}
