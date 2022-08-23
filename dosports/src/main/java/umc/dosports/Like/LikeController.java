package umc.dosports.Like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import umc.dosports.Like.model.*;

@Controller
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService){
        this.likeService = likeService;
    }

    //좋아요
    @PostMapping("")
    @ResponseBody
    public Object createLike(PostLikeReq form){
        long likeIdx = likeService.createLike(form);
        Map<String, Long> result = new HashMap<>();
        result.put("likeIdx", likeIdx);

        return result;
    }

    //좋아요 삭제
    @DeleteMapping("")
    @ResponseBody
    public void deleteLike(PostLikeReq form){
        //!!!!!Jwt수정 필요!!!!!
        long userIdxByJwt = 1;
        form.setUserIdx(userIdxByJwt);

        likeService.deleteLike(form, userIdxByJwt);
    }

    //좋아요 여부 확인
    @GetMapping("/check/{reviewIdx}")
    @ResponseBody
    public boolean checkLike(@PathVariable("reviewIdx") long reviewIdx){
        //!!!!!Jwt수정 필요!!!!!
        long userIdxByJwt = 1;

        return likeService.checkLike(reviewIdx, userIdxByJwt);
    }

    //좋아요 리뷰 목록
    @GetMapping("/{pageNum}")
    @ResponseBody
    public List<GetReviewRes> getLikeReview(@PathVariable("pageNum") int pageNum){
        //!!!!!Jwt수정 필요!!!!!
        long userIdxByJwt = 1;

        return likeService.getLikeReview(userIdxByJwt, pageNum);
    }
}
