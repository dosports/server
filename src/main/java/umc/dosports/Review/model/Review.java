package umc.dosports.Review.model;

enum level {
    high, middle, low
}

public class Review {
    private long reviewIdx;
    private long userIdx;
    private String title;
    private String img_path;
    private String contents;

    private level level;

    public umc.dosports.Review.model.level getLevel() {
        return level;
    }

    public void setLevel(umc.dosports.Review.model.level level) {
        this.level = level;
    }



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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
