package umc.dosports.Like;

import umc.dosports.Like.model.*;
import umc.dosports.Like.model.GetReviewRes;

import java.util.List;

public class LikeService {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository){
        this.likeRepository = likeRepository;
    }

    //좋아요
    public long createLike(Long userIdxByJWT, PostLikeReq likeReq){
        //좋아요 눌린 상태인지 체크
        if(!likeRepository.checkLike(likeReq.getReviewIdx(), userIdxByJWT)){
            long likeIdx = likeRepository.createLike(userIdxByJWT, likeReq);
            likeRepository.increaseLike(likeReq); //review테이블 like+1
            likeRepository.notifyLike(likeReq, likeIdx); //notify like
            return likeIdx;
        }
        return Long.parseLong("-1");
    }

    //좋아요 취소
    public void deleteLike(PostLikeReq likeReq, long userIdxByJWT){
        //if(!commentRepository.checkCommentExistts(commentIdx)) //오류 체크!
        //if(!commentRepository.checkUserEqual(commentIdx, userIdxByJWT)) //오류 체크!
        likeRepository.deleteLike(likeReq, userIdxByJWT);
    }

    //좋아요 체크
    public boolean checkLike(long reviewIdx, long userIdxByJWT){
        return likeRepository.checkLike(reviewIdx, userIdxByJWT);
    }

    //좋아요 리뷰 목록
    public GetReviewRes getLikeReview(long userIdxByJWT, int pageNum){
        //reviews
        List<Long> list = likeRepository.getLikeReviews(userIdxByJWT, pageNum);
        //endPage
        int endPage = likeRepository.getEndPage(userIdxByJWT);
        GetReviewRes getReviewRes = new GetReviewRes(list, endPage);

        return getReviewRes;
    }
}