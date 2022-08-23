package umc.dosports.Like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import umc.dosports.Like.model.*;
import umc.dosports.Like.model.GetReviewRes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService){
        this.likeService = likeService;
    }

    //좋아요 추가
    @PostMapping("")
    @ResponseBody
    public Object createLike(HttpServletRequest request, PostLikeReq form){
        long userIdxByJwt = Long.parseLong(String.valueOf(request.getAttribute("userIdx")));
        long likeIdx = likeService.createLike(userIdxByJwt, form);
        Map<String, Long> result = new HashMap<>();
        result.put("likeIdx", likeIdx);

        return result;
    }

    //좋아요 삭제
    @DeleteMapping("")
    @ResponseBody
    public void deleteLike(HttpServletRequest request, PostLikeReq form){
        long userIdxByJwt = Long.parseLong(String.valueOf(request.getAttribute("userIdx")));
        form.setUserIdx(userIdxByJwt);

        likeService.deleteLike(form, userIdxByJwt);
    }

    //좋아요 여부 확인
    @GetMapping("/check/{reviewIdx}")
    @ResponseBody
    public boolean checkLike(HttpServletRequest request, @PathVariable("reviewIdx") long reviewIdx){
        long userIdxByJwt = Long.parseLong(String.valueOf(request.getAttribute("userIdx")));

        return likeService.checkLike(reviewIdx, userIdxByJwt);
    }

    //좋아요 리뷰 목록
    @GetMapping("/{pageNum}")
    @ResponseBody
    public GetReviewRes getLikeReview(HttpServletRequest request, @PathVariable("pageNum") int pageNum){
        long userIdxByJwt = Long.parseLong(String.valueOf(request.getAttribute("userIdx")));

        return likeService.getLikeReview(userIdxByJwt, pageNum);
    }
}