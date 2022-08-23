package umc.dosports.Comment.model;

public class GetCommentRes {
    private long commentIdx;
    private long userIdx;
    private long reviewIdx;
    private String content;
    private long parentIdx;
    private String regDate;
    private String name;
    private String profileImg;

    public long getCommentIdx() {
        return commentIdx;
    }

    public void setCommentIdx(long commentIdx) {
        this.commentIdx = commentIdx;
    }

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

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}