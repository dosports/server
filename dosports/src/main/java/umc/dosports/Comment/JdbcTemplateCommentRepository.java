package umc.dosports.Comment;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import umc.dosports.Comment.model.GetCommentRes;
import umc.dosports.Comment.model.PostCommentReq;

import javax.sql.DataSource;
import java.util.List;

public class JdbcTemplateCommentRepository implements CommentRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateCommentRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
    댓글 작성
     */
    public int createComment(PostCommentReq com){
        String comQuery = "INSERT INTO comment (userIdx, reviewIdx, content, parentIdx)" +
                "VALUES (?, ?, ?, ?)";
        Object[] commentForm = new Object[]{
                com.getUserIdx(),
                com.getReviewIdx(),
                com.getContent(),
                com.getParentIdx()
        };
        this.jdbcTemplate.update(comQuery, commentForm);
        String idxQuery = "SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(idxQuery, int.class);
    }
    //review table comment+1
    public int increaseComment(PostCommentReq com){
        String countQuery = "SELECT comments FROM review WHERE reviewIdx = ?";
        int coms = this.jdbcTemplate.queryForObject(countQuery,
                (rs, rowNum) -> rs.getInt("comments"),
                com.getReviewIdx());
        String updateQuery = "UPDATE review SET comments = ? WHERE reviewIdx = ?";
        Object[] comsNum = new Object[]{coms+1, com.getReviewIdx()};
        return this.jdbcTemplate.update(updateQuery, comsNum);
    }
    //notify
    public void notifyComment(PostCommentReq com, long commentIdx){
        String findUserIdxQuery = "SELECT userIdx FROM review WHERE reviewIdx = ?";
        long userIdx = this.jdbcTemplate.queryForObject(findUserIdxQuery,
                (rs, rowNum) -> rs.getLong("userIdx"),
                com.getReviewIdx());
        String notifyQuery = "INSERT INTO notification (reviewIdx, userIdx, notiType, contentIdx)" +
                "VALUES(?, ?, ?, ?)";
        Object[] notify = new Object[]{com.getReviewIdx(), userIdx, 0, commentIdx};
        this.jdbcTemplate.update(notifyQuery, notify);
    }


    /*
    리뷰 댓굴 조회
     */
    public List<GetCommentRes> commentsByreviewIdx(long reviewIdx){
        String comQuery = "SELECT c.*, u.name, u.profileImg" +
                "FROM comment AS c" +
                "JOIN user AS a ON c.userIdx = u.userIdx" +
                "WHERE c.reviewIdx = ?";
        return jdbcTemplate.query(comQuery, this.commentRowMapper(), reviewIdx);
    }
    private RowMapper<GetCommentRes> commentRowMapper(){
        return (rs, rowNum) -> {
            GetCommentRes com = new GetCommentRes();
            com.setCommentIdx(rs.getLong("commentIdx"));
            com.setUserIdx(rs.getLong("userIdx"));
            com.setReviewIdx(rs.getLong("reviewIdx"));
            com.setContent(rs.getString("content"));
            com.setParentIdx(rs.getLong("parentIdx"));
            com.setRegDate(rs.getString("regDate"));
            com.setName(rs.getString("name"));
            com.setProfileImg(rs.getString("profileImg"));
            return com;
        };
    }


    /*
    작성자 여부 체크
     */
    public boolean checkUserEqual(long reviewIdx, long userIdxByJWT){
        String getUserIdxQuery = "SELECT userIdx FROM review WHERE reviewIdx = ?";
        long userIdx = this.jdbcTemplate.queryForObject(getUserIdxQuery,
                (rs, rowNum) -> rs.getLong("userIdx"),
                reviewIdx);
        if(userIdx == userIdxByJWT) return true;
        else return false;
    }


    /*
    댓글 삭제
     */
    public void deleteComment(long commentIdx){
        String deleteQuery = "DELETE FROM comment WHERE commentIdx = ?";
        this.jdbcTemplate.update(deleteQuery, commentIdx);
    }
}
