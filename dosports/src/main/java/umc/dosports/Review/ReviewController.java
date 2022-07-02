package umc.dosports.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private static final String UPLOAD_PATH = "C:\\Users\\user\\Desktop\\server\\dosports\\src\\main\\resources\\image";

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/post")
    @ResponseBody
    public List<Review> getAllReviews(){
        return reviewService.findReviews();
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