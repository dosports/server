package umc.dosports.Like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import umc.dosports.Like.model.GetReviewRes;
import umc.dosports.Like.model.PostLikeReq;

import javax.sql.DataSource;

public class LikeDao implements LikeRepository{
    private final JdbcTemplate jdbcTemplate;

    public LikeDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
    좋아요
     */
    public int createLike(PostLikeReq form){
        String likeQuery = "INSERT INTO like(reviewIdx, userIdx) VALUES(?, ?)";
        Object[] likeform = new Object[]{
                form.getReviewIdx(),
                form.getUserIdx()
        };
        this.jdbcTemplate.update(likeQuery, likeform);
        String idxQuery = "SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(idxQuery, int.class);
    }
    //review table like+1
    public int increaseLike(PostLikeReq like){
        String countQuery = "SELECT likes FROM review WHERE reviewIdx = ?";
        int likes = this.jdbcTemplate.queryForObject(countQuery,
                (rs, rowNum) -> rs.getInt("comments"),
                like.getReviewIdx());
        String updateQuery = "UPDATE review SET comments = ? WHERE reviewIdx = ?";
        Object[] comsNum = new Object[]{likes+1, like.getReviewIdx()};
        return this.jdbcTemplate.update(updateQuery, comsNum);
    }
    //notify
    public int notifyLike(PostLikeReq like, long userIdxByJWT){
        String notifyQuery = "INSERT INTO notify (reviewIdx, userIdx, notifyType)" +
                "VALUES(?, ?, ?)";
        Object[] notify = new Object[]{like.getReviewIdx(), userIdxByJWT, 1};
        this.jdbcTemplate.update(notifyQuery, notify);
        String idxQuery = "SELECT last_inser_id()";
        return this.jdbcTemplate.queryForObject(idxQuery, int.class);
    }


    /*
    좋아요 취소
     */
    public void deleteLike(PostLikeReq like, long userIdxByJWT){
        String deleteQuery = "DELETE FROM like WHERE reviewIdx = ? AND userIdx = ?";
        Object[] likeForm = new Object[]{
                like.getReviewIdx(),
                userIdxByJWT
        };
        this.jdbcTemplate.update(deleteQuery, likeForm);
    }


    /*
    좋아요 체크
     */
    public boolean checkLike(long reviewIdx, long userIdxByJWT){
        String checkQuery = "SELECT exists(SELECT likeIdx FROM like WHERE reviewIdx = ? AND userIdx = ?";
        Object[] check = new Object[]{
                reviewIdx,
                userIdxByJWT
        };
        return this.jdbcTemplate.queryForObject(checkQuery, boolean.class, check);
    }


    /*
    좋아요 리뷰 목록
     */
    public List<GetReviewRes> getLikeReview(long userIdxByJWT, int pageNum){
        String likeQuery = "SELECT * FROM r.* FROM review as r "+
                "JOIN like AS l ON l.reviewIdx = r.reviewIdx " +
                "WHERE l.userIdx = ?)";
        Object[] likeForm = new Object[]{
                userIdxByJWT,
                pageNum
        };
        return jdbcTemplate.query(likeQuery, this.likeReviewRowMapper(), likeForm);
    }
    private RowMapper<GetReviewRes> likeReviewRowMapper() {
        return (rs, rowNum) -> {
            GetReviewRes review = new GetReviewRes();
            review.setReviewIdx(rs.getInt("reviewIdx"));
            review.setUserIdx(rs.getInt("userIdx"));
            review.setName(rs.getString("name"));
            review.setImg_path(rs.getString("img_path"));
            review.setImg_path1(rs.getString("img_path1"));
            review.setImg_path2(rs.getString("img_path2"));
            review.setImg_path3(rs.getString("img_path3"));
            review.setImg_path4(rs.getString("img_path4"));
            review.setBrand(rs.getString("brand"));
            review.setTitle(rs.getString("title"));
            review.setCategory(rs.getString("category"));
            review.setSports(rs.getString("sports"));
            review.setRate(rs.getInt("rate"));
            review.setLikes(rs.getInt("likes"));
            review.setComments(rs.getInt("comments"));
            review.setGender(rs.getString("gender"));
            review.setHeight(rs.getInt("height"));
            review.setWeight(rs.getInt("weight"));
            review.setLevel(rs.getInt("level"));
            review.setSource(rs.getString("source"));
            review.setPrice(rs.getInt("price"));
            review.setContent(rs.getString("content"));
            review.setRegDate(rs.getString("regDate"));
            return review;
        };
    }
}
