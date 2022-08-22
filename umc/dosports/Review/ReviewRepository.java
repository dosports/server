package umc.dosports.Review;

import umc.dosports.Review.model.*;

import java.util.List;

public interface ReviewRepository {
    //Long save(SetReviewRequest review);


    //**********************************************게시글 조회 GET*****************************************************
    List<GetReviewRes> showReviews(MainPageReviewRequest review);
    List<GetReviewRes> showReviewIdxByFilter(String gender, String sports, GetReviewReq getReviewReq, boolean isPhoto, int sort_param, int page_num);
    List<GetReviewRes> showUserReview(long userIdx, int page_num);
    GetReviewRes showReviewByIdx(long reviewIdx);
    int increaseHits(long reviewIdx);

    //**********************************************게시글 생성 POST*****************************************************
    int createReview(PostReviewReq review);

    //**********************************************게시글 수정 PATCH*****************************************************
    int updateReview(long reviewIdx, String title, String content);

    //**********************************************게시글 삭제 DELETE****************************************************
    GetReviewRes deleteReview(GetReviewRes getReviewRes, long reviewIdx);

    //**********************************************데이터 유효 확인****************************************************
    boolean checkReviewExists(long reviewIdx);
    boolean checkUserEquals(long reviewIdx, long userIdxByJwt);
}
