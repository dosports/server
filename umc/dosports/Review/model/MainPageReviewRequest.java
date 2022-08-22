package umc.dosports.Review.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MainPageReviewRequest {
    private String sports;
    private int sort_param = 1;
    private int page_num = 1;
    private int review_num = 10;


    public String getSports() {
        if(sports == null) return "";
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public int getSort_param() {
        return sort_param;
    }

    public void setSort_param(int sort_param) {
        this.sort_param = sort_param;
    }

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public int getReview_num() {
        return review_num;
    }

    public void setReview_num(int review_num) {
        this.review_num = review_num;
    }
}
