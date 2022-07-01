package umc.dosports.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Long save(Review review);
    Optional<Review> findById(Long id);
    Optional<Review> findByTitle(String title);
    String update(Long id, Review review);
    String delete(Long id);
    List<Review> findAll();
}
