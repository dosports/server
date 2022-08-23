package umc.dosports.Comment;

import umc.dosports.Comment.model.*;

import java.util.List;

public interface CommentRepository {
    //댓글 작성
    int createComment(PostCommentReq commentReq);
    int increaseComment(PostCommentReq commentReq); //review table comment+1
    int notifyComment(PostCommentReq commentReq, long userIdxByJWT); //notify comment

    //리뷰 댓글 조회
    List<GetCommentRes> commentsByreviewIdx(long reviewIdx);

    //작성자와 유저 체크
    boolean checkUserEqual(long reviewIdx, long userIdxByJWT);

    //댓글 삭제
    void deleteComment(long commentIdx);
}
