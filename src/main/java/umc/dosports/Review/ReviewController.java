package umc.dosports.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.dosports.Review.model.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private static final String UPLOAD_PATH = "C:\\Users\\이석준\\Desktop\\dosports\\dosports\\src\\main\\resources\\image";


    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/post/{sports}/{part}/{brand}")
    @ResponseBody
    public List<Review> getAllReviews(@PathVariable("sports") String sports, @PathVariable("part") String part,
                                      @PathVariable("brand") String brand, Filter filter){
        Category category = new Category();
        category.setCategory(sports, part, brand);
        return reviewService.findReviews(category, filter);
    }

    @PostMapping("/post")
    @ResponseBody
    public Object create(ReviewForm form, @RequestParam MultipartFile file) throws IOException {
        Review review = new Review();
        review.setUserIdx(form.getUserIdx());
        review.setTitle(form.getTitle());
        review.setContents(form.getContents());
        review.setImg_path(saveFile(file));

        long reviewIdx = reviewService.create(review);
        Map<String, Long> result = new HashMap<>();
        result.put("reviewIdx", reviewIdx);

        return result;
    }

    private String saveFile(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String img_path = uuid + "_" + file.getOriginalFilename();

        File dest = new File(UPLOAD_PATH, file.getOriginalFilename());
        file.transferTo(dest);

        return img_path;
    }

    @DeleteMapping("/post/{idx}")
    @ResponseBody
    public String deleteReview(@PathVariable(name = "idx") Long idx) {
        return reviewService.deleteReview(idx);
    }
}