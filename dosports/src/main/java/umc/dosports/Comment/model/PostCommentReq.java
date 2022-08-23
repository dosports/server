package umc.dosports.Comment.model;

public class PostCommentReq {
    private long userIdx;
    private long reviewIdx;
    private String content;
    private long parentIdx;

    public long getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(long userIdx) {
        this.userIdx = userIdx;
    }

    public long getReviewIdx() {
        return reviewIdx;
    }

    public void setReviewIdx(long reviewIdx) {
        this.reviewIdx = reviewIdx;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getParentIdx() {
        return parentIdx;
    }

    public void setParentIdx(long parentIdx) {
        this.parentIdx = parentIdx;
    }
}
