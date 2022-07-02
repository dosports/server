package umc.dosports.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private static final String UPLOAD_PATH = "C:\\Users\\JeaYoung Kim\\Desktop\\dosports\\src\\main\\resources\\image";

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /*모든 리뷰*/
    @GetMapping("/review")
    @ResponseBody
    public List<Review> getAllReviews(){
        return reviewService.findReviews();
    }

    /*리뷰작성페이지*/
    @GetMapping("/review/new")
    public String createReview() {
        return "reviews/createReviewForm";
    }
    /*리뷰작성form*/
    @PostMapping("/review")
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

    /*사진저장*/
    private String saveFile(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String img_path = uuid + "_" + file.getOriginalFilename();

        File dest = new File(UPLOAD_PATH, file.getOriginalFilename());
        file.transferTo(dest);

        return img_path;
    }

    /*리뷰 삭제하기*/
    @DeleteMapping("/review/{idx}")
    @ResponseBody
    public String deleteReview(@PathVariable(name = "idx") Long idx) {
        return reviewService.deleteReview(idx);
    }

    /*리뷰 상세보기*/
    @GetMapping("/review/{idx}")
    @ResponseBody
    public Optional<Review> getReview(@PathVariable(name = "idx") Long idx){
        return reviewService.findOne(idx);
    }
}