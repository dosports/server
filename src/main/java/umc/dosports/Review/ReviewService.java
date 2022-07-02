package umc.dosports.Review;

import umc.dosports.Review.model.*;

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


    public List<Review> findReviews(Category category, Filter filter) {
        return reviewRepository.findReviews(category, filter);
    }

    public Optional<Review> findOne(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public String deleteReview(Long id) {
        return reviewRepository.delete(id);
    }
}