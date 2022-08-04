package umc.dosports.Review;

import umc.dosports.Review.model.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    //게시글 생성
    public int createReview(SetReviewRequest review) {
        return reviewRepository.createReview(review);
    }


    public List<GetReviewRequest> retrieveReviewsByFilter(String gender, String sports, Filter filter) {
        List<Integer> indexes = reviewRepository.showReviews(gender, sports, filter);
        List<GetReviewRequest> reviews = new LinkedList<>();
        for(int i:indexes){
            //System.out.println("checkPoint: "+i);
            reviews.add(reviewRepository.showReviewByIdx(i));
        }
        return reviews;
    }

    public GetReviewRequest retrieveReviews(int reviewIdx) {
        return reviewRepository.showReviewByIdx(reviewIdx);
    }

    public List<GetReviewRequest> retrieveUserReview(int userIdx){ return reviewRepository.showUserReview(userIdx);}

    public String updateReview(int reviewIdx, String content){
        int result = reviewRepository.updateReview(reviewIdx, content);
        if(result == 0){
            return "수정 실패";
        }
        return "수정 성공";
    }

    public GetReviewRequest deleteReview(int reviewIdx) {
        //받은 리뷰인덱스가 유효한지 확인
        if(!reviewRepository.checkReviewExists(reviewIdx)){
            return null;
        }

        //해당 리뷰의 작성날짜와 현재 날짜를 비교
        GetReviewRequest getReviewRequest = reviewRepository.showReviewByIdx(reviewIdx);
        String[] regDate = getReviewRequest.getRegDate().split(" ")[0].split("-");
        String[] curDate = LocalDate.now().toString().split("-");
        int reg = Integer.parseInt(regDate[0]+regDate[1]+regDate[2]);
        int cur = Integer.parseInt(curDate[0]+curDate[1]+curDate[2]);
        if(cur-reg <= 100) return reviewRepository.deleteReview(getReviewRequest, reviewIdx);
        else return null;
    }

}