package umc.dosports.Review.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GetReviewUserInfoRes {
    private long userIdx;
    private String gender;
    private int height;
    private int weight;

    public GetReviewUserInfoRes(long userIdx, String gender, int height, int weight) {
        this.userIdx = userIdx;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }

    public long getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(long userIdx) {
        this.userIdx = userIdx;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
