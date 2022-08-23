package umc.dosports.Like.model;

public class PostLikeReq {
    private long reviewIdx;
    private long userIdx;

    public long getReviewIdx() {
        return reviewIdx;
    }

    public void setReviewIdx(long reviewIdx) {
        this.reviewIdx = reviewIdx;
    }

    public long getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(long userIdx) {
        this.userIdx = userIdx;
    }
}