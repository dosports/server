package umc.dosports.Review;

import umc.dosports.Review.model.*;

import java.time.LocalDate;
import java.util.List;

public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    //**********************************************게시글 조회 GET*****************************************************
    public List<GetReviewRes> retrieveReviews(MainPageReviewRequest review){
        return reviewRepository.showReviews(review);
    }
    public List<GetReviewRes> retrieveReviewsByFilter(String gender, String sports, GetReviewReq getReviewReq, boolean isPhoto, int sort_param, int pageNum) {
        return reviewRepository.showReviewIdxByFilter(gender, sports, getReviewReq,isPhoto, sort_param, pageNum);
    }
    public List<GetReviewRes> retrieveUserReview(long userIdx, int pageNum){
        return reviewRepository.showUserReview(userIdx, pageNum);
    }
    public GetReviewRes retrieveReviewsByIdx(long reviewIdx) {
        reviewRepository.increaseHits(reviewIdx);
        return reviewRepository.showReviewByIdx(reviewIdx);
    }

    //**********************************************게시글 작성 POST*****************************************************
    public int createReview(PostReviewReq review) {
        return reviewRepository.createReview(review);
    }

    //**********************************************게시글 수정 PATCH*****************************************************
    public PatchReviewRes updateReview(long reviewIdx, String title, String content, long userIdxByJwt){
        if(!reviewRepository.checkUserEquals(reviewIdx, userIdxByJwt)){
            System.out.println("불일치");
            return null;
        }
        return reviewRepository.updateReview(reviewIdx, title, content);
    }

    //**********************************************게시글 삭제 DELETE*****************************************************
    public GetReviewRes deleteReview(long reviewIdx, long userIdxByJwt) {
        //받은 리뷰인덱스가 유효한지 확인
        if(!reviewRepository.checkReviewExists(reviewIdx)){
            return null;
        }
        if(!reviewRepository.checkUserEquals(reviewIdx, userIdxByJwt)){
            System.out.println("불일치");
            return null;
        }

        //해당 리뷰의 작성날짜와 현재 날짜를 비교
        GetReviewRes getReviewRes = reviewRepository.showReviewByIdx(reviewIdx);
        String[] regDate = getReviewRes.getRegDate().split(" ")[0].split("-");
        String[] curDate = LocalDate.now().toString().split("-");
        int reg = Integer.parseInt(regDate[0]+regDate[1]+regDate[2]);
        int cur = Integer.parseInt(curDate[0]+curDate[1]+curDate[2]);
        if(cur-reg <= 100) return reviewRepository.deleteReview(getReviewRes, reviewIdx);
        else return null;
    }

}