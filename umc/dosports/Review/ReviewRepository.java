package umc.dosports.Review;

import umc.dosports.Review.model.*;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    //Long save(SetReviewRequest review);

    int createReview(SetReviewRequest review);
    List<Integer> showReviews(String gender, String sports, Filter filter);

    GetReviewRequest showReviewByIdx(int reviewIdx);
    List<GetReviewRequest> showUserReview(int userIdx);
    int updateReview(int reviewIdx, String content);
    GetReviewRequest deleteReview(GetReviewRequest getReviewRequest, int reviewIdx);

    boolean checkReviewExists(int reviewIdx);
}
