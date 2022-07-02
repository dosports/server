package umc.dosports.Review;

import umc.dosports.Review.model.*;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Long save(Review review);
    Optional<Review> findById(Long id);
    Optional<Review> findByTitle(String title);
    String update(Long id, Review review);
    String delete(Long id);
    List<Review> findAll();
    List<Review> findReviews(Category category, Filter filter);
}
