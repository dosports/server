package umc.dosports.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.dosports.Review.model.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private static final String UPLOAD_PATH = "C:\\Users\\seokj\\석준\\server-tol_e\\src\\main\\resources\\image";


    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{gender}/{sports}")
    @ResponseBody
    public List<GetReviewRequest> getReviewsByFilter(@PathVariable(name="gender") String gender, @PathVariable(name="sports") String sports,
                                             @RequestParam(required = false) Object category, @RequestParam(required = false) Object height, @RequestParam(required = false) Object weight,
                                             @RequestParam(required = false) Object level, @RequestParam(required = false) Object max_price, @RequestParam(required = false) Object min_price){
        return reviewService.retrieveReviewsByFilter(gender, sports, new Filter(category, height, weight, level, max_price, min_price));
    }

    @GetMapping("/{reviewIdx}")
    @ResponseBody
    public GetPreciseReviewRequest getPreciseReview(@PathVariable(name="reviewIdx") int reviewIdx){
        GetReviewRequest reviewRequest = reviewService.retrieveReviews(reviewIdx);
        return null;
    }

    @GetMapping("/user/{userIdx}")
    @ResponseBody
    public List<GetReviewRequest> getUserReview(@PathVariable(name="userIdx") int userIdx){
        return reviewService.retrieveUserReview(userIdx);
    }

    @PostMapping("/post")
    @ResponseBody
    public Object createReview(SetReviewRequest form, @RequestParam(required=false) List<MultipartFile>  file) throws IOException {
        System.out.println(form.getUserIdx());
        form.setImg_path(saveFile(file));

        int reviewIdx = reviewService.createReview(form);
        Map<String, Integer> result = new HashMap<>();
        result.put("reviewIdx", reviewIdx);

        return result;
    }

    private List<String> saveFile(List<MultipartFile> file) throws IOException {
        List<String> temp = new ArrayList<>(5);
        for(MultipartFile f:file) {
            UUID uuid = UUID.randomUUID();
            String img_path = uuid + "_" + f.getOriginalFilename();

            File dest = new File(UPLOAD_PATH, f.getOriginalFilename());
            f.transferTo(dest);
            temp.add(img_path);
        }
        while(temp.size() != 5){
            temp.add(null);
        }
        return temp;
    }

    @PatchMapping("/{reviewIdx}")
    @ResponseBody
    public String updateReview(@PathVariable(name = "reviewIdx") int reviewIdx, @RequestBody String content) {
        return reviewService.updateReview(reviewIdx, content);
    }

    @DeleteMapping("/{reviewIdx}")
    @ResponseBody
    public GetReviewRequest deleteReview(@PathVariable(name = "reviewIdx") int reviewIdx) {
        return reviewService.deleteReview(reviewIdx);
    }
}