package umc.dosports.Review;

import java.util.List;
import java.util.Optional;

public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    //게시글 생성
    public Long create(Review review) {
        return reviewRepository.save(review);
    }


    public List<Review> findReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> findOne(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public String deleteReview(Long id) {
        return reviewRepository.delete(id);
    }
}