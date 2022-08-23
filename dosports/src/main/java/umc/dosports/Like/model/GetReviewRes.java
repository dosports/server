package umc.dosports.Like.model;

public class GetReviewRes {
    private List<Long> reviewIdx;
    private int endPage;

    public GetReviewRes(List<Long> list, int endPage){
        this.reviewIdx = list;
        this.endPage = endPage;
    }

    public List<Long> getReviewIdx() {
        return reviewIdx;
    }

    public void setReviewIdx(List<Long> reviewIdx) {
        this.reviewIdx = reviewIdx;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
}
