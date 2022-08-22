package umc.dosports.Review;

import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("")
    @ResponseBody
    public List<GetReviewRes> getReviews(@RequestBody(required = false) MainPageReviewRequest reviews){
        if(reviews == null) reviews = new MainPageReviewRequest();
        return reviewService.retrieveReviews(reviews);
    }

    @GetMapping("/{gender}/{sports}")
    @ResponseBody
    public List<GetReviewRes> getReviewsByFilter(@PathVariable(name="gender") String gender, @PathVariable(name="sports") String sports,
                                                 @RequestParam(required = false) Object category, @RequestParam(required = false) Object height,
                                                 @RequestParam(required = false) Object weight, @RequestParam(required = false) Object level,
                                                 @RequestParam(required = false) Object max_price, @RequestParam(required = false) Object min_price,
                                                 @RequestParam(required = false, defaultValue = "false") Object isPhoto,
                                                 @RequestParam(required = false, defaultValue = "1") Object sort_param,
                                                 @RequestParam(required = false, defaultValue = "1") Object page_num) {
        if(!gender.equals("f") && !gender.equals("m")) return null;
        return reviewService.retrieveReviewsByFilter(gender, sports, new GetReviewReq(category, height, weight, level, max_price, min_price),
                Boolean.parseBoolean((String)isPhoto), Integer.parseInt((String) sort_param), Integer.parseInt((String) page_num));
    }


    @GetMapping("/{reviewIdx}")
    @ResponseBody
    public GetReviewRes getPreciseReview(@PathVariable(name="reviewIdx") long reviewIdx){
        GetReviewRes reviewRequest = reviewService.retrieveReviewsByIdx(reviewIdx);
        return reviewRequest;
    }

    @GetMapping("/user/{userIdx}")
    @ResponseBody
    public List<GetReviewRes> getUserReview(@PathVariable(name="userIdx") long userIdx, @RequestParam(required=false, defaultValue = "1") Object page_num){
        return reviewService.retrieveUserReview(userIdx, Integer.parseInt((String)page_num));
    }

    @PostMapping("/post")
    @ResponseBody
    public Object createReview(PostReviewReq form, @RequestParam(required=false) List<MultipartFile>  file) throws IOException {

            //form.setUserIdx(TokenProvider.getUserIdx());
            form.setUserIdx(5); //위 주석 해제 시 삭제 요망
            form.setImg_path(saveFile(file));

            long reviewIdx = reviewService.createReview(form);
            Map<String, Long> result = new HashMap<>();
            result.put("reviewIdx", reviewIdx);

            return result;
    }

    private List<String> saveFile(List<MultipartFile> file) throws IOException {
        List<String> temp = new ArrayList<>(5);
        if(file != null) {
            for (MultipartFile f : file) {
                UUID uuid = UUID.randomUUID();
                String img_path = uuid + "_" + f.getOriginalFilename();

                File dest = new File(UPLOAD_PATH, f.getOriginalFilename());
                f.transferTo(dest);
                temp.add(img_path);
            }
        }
        while(temp.size() != 5){
            temp.add(null);
        }
        return temp;
    }

    @PatchMapping("/{reviewIdx}")
    @ResponseBody
    public PatchReviewRes updateReview(@PathVariable(name = "reviewIdx") long reviewIdx, @RequestBody PatchReviewReq patchReviewReq) {
        long userIdxByJwt = 5; //TokenProvider.getUserIdx();
        String title = patchReviewReq.getTitle();
        String content = patchReviewReq.getContent();
        return reviewService.updateReview(reviewIdx, title, content, userIdxByJwt);
    }

    @DeleteMapping("/{reviewIdx}")
    @ResponseBody
    public GetReviewRes deleteReview(@PathVariable(name = "reviewIdx") long reviewIdx) {
        long userIdxByJwt = 4; //TokenProvider.getUserIdx();
        return reviewService.deleteReview(reviewIdx, userIdxByJwt);
    }

}