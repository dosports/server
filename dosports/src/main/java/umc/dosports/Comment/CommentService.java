package umc.dosports.Comment;

import umc.dosports.Comment.model.*;

import java.util.List;

public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    //댓글 작성
    public long createComment(PostCommentReq commentReq){
        long commentIdx = commentRepository.createComment(commentReq);
        commentRepository.increaseComment(commentReq); //review 테이블 comment + 1
        commentRepository.notifyComment(commentReq, commentIdx); //notify comment
        return commentIdx;
    }

    //리뷰 댓글 조회
    public List<GetCommentRes> getCommentsByreviewIdx(long reviewIdx){
        return commentRepository.commentsByreviewIdx(reviewIdx);
    }

    //작성자 여부 체크
    public boolean checkUserEqual(long commentIdx, long userIdxByJWT){
        return commentRepository.checkUserEqual(commentIdx, userIdxByJWT);
    }

    //댓글 삭제
    public void deleteComment(long commentIdx, long userIdxByJWT){
        //if(!commentRepository.checkCommentExistts(commentIdx)) //오류 체크!
        //if(!commentRepository.checkUserEqual(commentIdx, userIdxByJWT)) //오류 체크!
        commentRepository.deleteComment(commentIdx);
    }
}
