package umc.dosports.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import umc.dosports.Comment.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    //댓글 작성
    @PostMapping("")
    @ResponseBody
    public Object createComment(HttpServletRequest request, PostCommentReq form){
        Long userIdxByJWT = Long.parseLong(String.valueOf(request.getAttribute("userIdx")));
        long commentIdx = commentService.createComment(userIdxByJWT, form);
        Map<String, Long> result = new HashMap<>();
        result.put("commentIdx", commentIdx);

        return result;
    }

    //리뷰 댓글 조회
    @GetMapping("/{reviewIdx}")
    @ResponseBody
    public List<GetCommentRes> getComments(@PathVariable("reviewIdx") long reviewIdx){
        return commentService.getCommentsByreviewIdx(reviewIdx);
    }

    //작성자 체크
    @GetMapping("/check/{commentIdx}")
    @ResponseBody
    public boolean checkUserEqual(HttpServletRequest request, @PathVariable("commentIdx") long commentIdx){
        long userIdxByJWT = Long.parseLong(String.valueOf(request.getAttribute("userIdx")));
        return commentService.checkUserEqual(commentIdx, userIdxByJWT);
    }

    //리뷰 댓글 삭제
    @DeleteMapping("/delete/{commentIdx}")
    @ResponseBody
    public void deleteComment(HttpServletRequest request, @PathVariable("commentIdx") long commentIdx){
        long userIdxByJWT = Long.parseLong(String.valueOf(request.getAttribute("userIdx")));
        commentService.deleteComment(commentIdx, userIdxByJWT);
    }
}