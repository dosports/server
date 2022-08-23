package umc.dosports.Notification.model;

public class GetNotiRes {
    private long notiIdx;
    private long userIdx;
    private long reviewIdx;
    private int notiType;
    private String regDate;

    public long getNotiIdx() {
        return notiIdx;
    }

    public void setNotiIdx(long notiIdx) {
        this.notiIdx = notiIdx;
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

    public int getNotiType() {
        return notiType;
    }

    public void setNotiType(int notiType) {
        this.notiType = notiType;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
