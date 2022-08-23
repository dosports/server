package umc.dosports.Notification;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import umc.dosports.Notification.model.DeleteNotiReq;
import umc.dosports.Notification.model.GetNotiRes;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.List;

public class NotiDao implements NotiRepository{
    private final JdbcTemplate jdbcTemplate;

    public NotiDao(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
    알림 조회
     */
    public List<GetNotiRes> getNotis(long userIdxByJWT){
        String notiQuery = "SELECT n.*, c.content " +
                "FROM notify AS n" +
                "JOIN comment AS c ON c.commentIdx = n.contentIdx" +
                "WHERE userIdx = ? AND isRead = ?";
        Object[] notiForm = new  Object[]{
                userIdxByJWT,
                0
        };
        return jdbcTemplate.query(notiQuery, this.notiRowMapper(), notiForm);
    }
    private RowMapper<GetNotiRes> notiRowMapper(){
        return (rs, rowNum) -> {
            GetNotiRes noti = new GetNotiRes();
            noti.setNotiIdx(rs.getLong("notiIdx"));
            noti.setUserIdx(rs.getLong("userIdx"));
            noti.setReviewIdx(rs.getLong("reviewIdx"));
            noti.setNotiType(rs.getInt("notiType"));
            noti.setRegDate(rs.getString("regDate"));
            noti.setContent(rs.getString("content"));
            return noti;
        };
    }


    /*
    알림 읽음
     */
    public void patchNoti(long notiIdx){
        String patchQuery = "UPDATE notify SET isRead = ?";
        this.jdbcTemplate.update(patchQuery, notiIdx);
    }


    /*
    알림 삭제
     */
    public void deleteNoti(DeleteNotiReq form){
        String deleteQuery = "DELETE FROM notify WHERE notifyIdx = ?";
        this.jdbcTemplate.update(deleteQuery, form.getNotiIdx());
    }

}
