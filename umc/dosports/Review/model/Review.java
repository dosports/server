package umc.dosports.Review.model;


import java.util.*;

public class Review {
    /*
    "reviewIdx": 1,
		"userName": "이석준",
		"regDate": "2020/07/21 00:06:11",
		"img_path0": "",
		"img_path1": "",
		"img_path2": "",
		"img_path3": "",
		"img_path4": "",
		"brand": "나이키",
		"title": "나이키상의",
		"category": "상의",
		"hits": 2,
		"likes": 4,
		"comments": 2,
		"gender": "female",
		"height": "179",
		"weight": "65",
		"level": "1",
		"source": "나이키 공홈",
		"price": "12000",
		"content": "솰라솰라솰라솰라솰라솰라"
     */
    private long userIdx;
    private String title;
    private String img_path;
    private String contents;

    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
