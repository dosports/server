package umc.dosports.Like;

import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import umc.dosports.Like.model.GetReviewIdxRes;
import umc.dosports.Like.model.GetReviewRes;
import umc.dosports.Like.model.PostLikeReq;

import javax.sql.DataSource;
import java.util.*;

public class JdbcTemplateLikeRepository implements LikeRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateLikeRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
    좋아요
     */
    public int createLike(PostLikeReq form, long userIdxByJWT){
        String likeQuery = "INSERT INTO likeTable(reviewIdx, userIdx) VALUES(?, ?)";
        Object[] likeform = new Object[]{
                form.getReviewIdx(),
                userIdxByJWT
        };
        this.jdbcTemplate.update(likeQuery, likeform);
        String idxQuery = "SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(idxQuery, int.class);
    }
    //review table like+1
    public int increaseLike(PostLikeReq like){
        String countQuery = "SELECT likes FROM review WHERE reviewIdx = ?";
        int likes = this.jdbcTemplate.queryForObject(countQuery,
                (rs, rowNum) -> rs.getInt("likes"),
                like.getReviewIdx());
        String updateQuery = "UPDATE review SET comments = ? WHERE reviewIdx = ?";
        Object[] comsNum = new Object[]{likes+1, like.getReviewIdx()};
        return this.jdbcTemplate.update(updateQuery, comsNum);
    }
    //notify
    public void notifyLike(PostLikeReq like, long likeIdx){
        String findUserIdxQuery = "SELECT userIdx FROM review WHERE reviewIdx = ?";
        long userIdx = this.jdbcTemplate.queryForObject(findUserIdxQuery,
                (rs, rowNum) -> rs.getLong("userIdx"),
                like.getReviewIdx());
        String notifyQuery = "INSERT INTO notification (reviewIdx, userIdx, notiType, contentIdx)" +
                "VALUES(?, ?, ?, ?)";
        Object[] notify = new Object[]{like.getReviewIdx(), userIdx, 1, likeIdx};
        this.jdbcTemplate.update(notifyQuery, notify);
    }


    /*
    좋아요 취소
     */
    public void deleteLike(PostLikeReq like, long userIdxByJWT){
        String deleteQuery = "DELETE FROM likeTable WHERE reviewIdx = ? AND userIdx = ?";
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
        String checkQuery = "SELECT exists(SELECT likeIdx FROM likeTable WHERE reviewIdx = ? AND userIdx = ?)";
        Object[] check = new Object[]{
                reviewIdx,
                userIdxByJWT
        };
        return this.jdbcTemplate.queryForObject(checkQuery, boolean.class, check);
    }


    /*
    좋아요 리뷰 목록
     */
    public List<Long> getLikeReviews(long userIdxByJWT, int pageNum){
        String likeQuery = "SELECT p.pagingIdx, p.reviewIdx FROM " +
                "(SELECT l.*, ROW_NUMBER() OVER(ORDER BY l.likeIdx DESC) AS pagingIdx " +
                "FROM likeTable AS l WHERE userIdx = ?) AS p " +
                "WHERE p.pagingIdx BETWEEN (10 * (?) + 1) AND (10 * (?)); ";
        Object[] likeForm = new Object[]{
                userIdxByJWT,
                pageNum - 1,
                pageNum
        };
        return jdbcTemplate.query(likeQuery, this.likeReviewsRowMapper(), likeForm);
    }
    private RowMapper<Long> likeReviewsRowMapper() {
        return (rs, rowNum) -> {
            Long reviewIdx;
            reviewIdx = rs.getLong("reviewIdx");
            return reviewIdx;
        };
    }
    public int getEndPage(long userIdxByJWT){
        String countQuery = "SELECT COUNT(likeIdx) FROM likeTable WHERE userIdx = ?";
        int total = jdbcTemplate.queryForObject(countQuery, int.class, userIdxByJWT);
        return ((total - 1) / 10) + 1;
    }
}