package umc.dosports.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import umc.dosports.Comment.model.*;

import java.util.*;

@Controller
@RequestMapping("/comment")
public class CommentConroller {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    //댓글 작성
    @PostMapping("")
    @ResponseBody
    public Object createComment(PostCommentReq form){
        //!!!!!Jwt수정 필요!!!!!
        long userIdxByJwt = 1;
        form.setUserIdx(userIdxByJwt);

        long commentIdx = commentService.createComment(form, userIdxByJwt);
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
    public boolean checkUserEqual(@PathVariable("commentIdx") long commentIdx){
        //!!!!!JWT 수정 필요!!!!!!
        long userIdxByJWT = 1;
        return commentService.checkUserEqual(commentIdx, userIdxByJWT);
    }

    //리뷰 댓글 삭제
    @DeleteMapping("/{commentIdx}")
    @ResponseBody
    public void deleteComment(@PathVariable("commentIdx") long commentIdx){
        //!!!!!JWT 수정 필요!!!!!!
        long userIdxByJWT = 1;
        commentService.deleteComment(commentIdx ,userIdxByJWT);
    }
}
