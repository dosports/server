package umc.dosports.Review;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import umc.dosports.Review.model.*;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.*;

public class JdbcTemplateReviewRepository implements ReviewRepository{

    private final JdbcTemplate jdbcTemplate;


    public JdbcTemplateReviewRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetReviewRes> showReviews(MainPageReviewRequest review){
        String findQuery = "select * from ( select ROW_NUMBER() OVER("+sortString(review.getSort_param())+") as rownum, r.* ,u.name "+
                "from review as r join user as u on u.userIdx = r.userIdx "+
                ((!review.getSports().equals(""))?"where r.sports = ?":"where ?")+
                ") r where rownum between (?*?) and (?*?) group by r.reviewIdx";
        Object[] findQueryParam = new Object[]{(review.getSports().equals(""))?-1:review.getSports() , review.getReview_num(), review.getPage_num()-1, review.getReview_num(), review.getPage_num()};
        return jdbcTemplate.query(findQuery, reviewRowMapper(), findQueryParam);
    }

    public List<GetReviewRes> showReviewIdxByFilter(String gender, String sports, GetReviewReq getReviewReq, boolean isPhoto, int sort_param, int page_num){   //category의 저장방식 수정!! 문자열로 변환시 오류 발생
        String findQuery = "select * from ( select ROW_NUMBER() OVER("+sortString(sort_param)+") as rownum, r.* ,u.name "+
                "from review as r join user as u on r.userIdx = u.userIdx where r.gender = ? and r.sports = ? " +
                ((!getReviewReq.getCategory().equals(""))?"and r.category = ? ":"and ? ")+
                ((getReviewReq.getHeight() == -1)?"and r.height between ? and ? ":"and ? and ? ")+
                ((getReviewReq.getWeight() != -1)?"and r.weight between ? and ? ":"and ? and ? ")+
                ((getReviewReq.getLevel() != -1)?"and r.level = ? ":"and ? ")+
                ((getReviewReq.getMin_price() != -1 && getReviewReq.getMax_price() != -1)?"and r.price between ? and ? ":
                        ((getReviewReq.getMin_price() != -1)?"and r.price >= ? ":"and ? ")+
                                ((getReviewReq.getMax_price() != -1)?"and r.price <= ? ":"and ? "))+
                ((isPhoto)?" and r.img_path is not null":"")+
                ") r where rownum between (10*(?)) and (10*(?)) group by r.reviewIdx";

        Object[] findQueryParam = new Object[]{gender, sports, (getReviewReq.getCategory().equals(""))?-1: getReviewReq.getCategory(),
                getReviewReq.getHeight()-5, getReviewReq.getHeight()+5, getReviewReq.getWeight()-5, getReviewReq.getWeight()+5, getReviewReq.getLevel(),
                getReviewReq.getMin_price(), getReviewReq.getMax_price(), page_num-1, page_num}; //문자열 부분이 빌 경우 필터가 제대로 작동을 안하여 if문을 통해 조정해주었다.

        return jdbcTemplate.query(findQuery, reviewRowMapper(), findQueryParam);
    }

    public List<GetReviewRes> showUserReview(long userIdx, int page_num){
        String findQuery = "select * from ( select ROW_NUMBER() OVER(order by r.regDate desc) as rownum, r.* ,u.name "+
                "from review as r "+
                "join user as u on u.userIdx = ? "+
                "where u.userIdx = r.userIdx" +
                ") r where rownum between (10*(?)) and (10*(?)) " +
                "group by r.reviewIdx";
        Object[] findQueryParam = new Object[] {userIdx, page_num-1, page_num};
        return jdbcTemplate.query(findQuery, this.reviewRowMapper(), findQueryParam);
    }

    public GetReviewRes showReviewByIdx(long idx){
        String findQuery = "select r.*, u.name "+
                "from review as r "+
                "join user as u on u.userIdx = r.userIdx " +
                "where r.reviewIdx = ? " +
                "group by r.reviewIdx";
        long findQueryParam = idx;
        return jdbcTemplate.queryForObject(findQuery, this.reviewRowMapper(), findQueryParam);
    }

    public int increaseHits(long reviewIdx){
        String findHits = "select hits from review where reviewIdx = ?";
        int hits = this.jdbcTemplate.queryForObject(findHits, (rs, rowNum) -> rs.getInt("hits"), reviewIdx);
        String modifyReviewQuery = "update review set hits = ? where reviewIdx = ? ";
        Object[] modifyReviewParams = new Object[]{hits+1, reviewIdx};
        return this.jdbcTemplate.update(modifyReviewQuery,modifyReviewParams);
    }




    @Override
    public int createReview(PostReviewReq review){
        String createReviewQuery = "insert into review (userIdx, title, img_path, img_path1, img_path2, img_path3, img_path4, " +
                "content, rate, brand, sports, category, gender, height, weight, source, price, level)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createReviewParams = new Object[]{review.getUserIdx(), review.getTitle(), review.getImg_path().get(0)
                , review.getImg_path().get(1), review.getImg_path().get(2), review.getImg_path().get(3), review.getImg_path().get(4)
                , review.getContent(), review.getRate(), review.getBrand(), review.getSports(), review.getCategory(), review.getGender(),
                review.getHeight(), review.getWeight(), review.getSource(), review.getPrice(), review.getLevel()};
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
    @Override
    public PatchReviewRes updateReview(long reviewIdx, String title, String content) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String modifyReviewQuery = "update review set title = ?, content = ?, updateDate=? where reviewIdx = ? ";
        Object[] modifyReviewParams = new Object[]{title, content,sdf1.format(new Date()), reviewIdx};
        this.jdbcTemplate.update(modifyReviewQuery,modifyReviewParams);
        String patchReviewQuery = "select title, content, updateDate from review where reviewIdx = ?";
        return this.jdbcTemplate.queryForObject(patchReviewQuery, (rs, rowNum)->{
            PatchReviewRes review = new PatchReviewRes();
            review.setTitle(rs.getString("title"));
            review.setContent(rs.getString("content"));
            review.setUpdateDate(rs.getString("updateDate"));
            return review;
        }, reviewIdx);
    }

    @Override
    public GetReviewRes deleteReview(GetReviewRes getReviewRes, long reviewIdx) {
        this.jdbcTemplate.update("delete from review where reviewIdx = ?",reviewIdx);
        return getReviewRes;
    }

    public boolean checkReviewExists(long reviewIdx){
        String checkReviewExistQuery = "select exists(select reviewIdx from review where reviewIdx = ?)";
        long checkReviewExistParams = reviewIdx;
        return this.jdbcTemplate.queryForObject(checkReviewExistQuery,
                boolean.class,
                checkReviewExistParams);
    }


    public boolean checkUserEquals(long reviewIdx, long userIdxByJwt){
        String checkReviewExistQuery = "select userIdx from review where reviewIdx = ?";
        Long userIdx = this.jdbcTemplate.queryForObject(checkReviewExistQuery,
                (rs, rowNum) -> rs.getLong("userIdx"), reviewIdx);
        return (userIdxByJwt == userIdx);
    }


    private String sortString(int sort_param){   //정렬 기준
        String str = " order by ";
        switch (sort_param){
            case 1: str+="r.regDate"; break;
            case 2: str+="r.likes"; break;
            case 3: str+="r.price"; break;
            case 4: str+="r.hits"; break;
        }
        str+=" desc";
        return str;
    }

    private RowMapper<GetReviewRes> reviewRowMapper() {
        return (rs, rowNum) -> {
            GetReviewRes review = new GetReviewRes();
            review.setReviewIdx(rs.getInt("reviewIdx"));
            review.setUserIdx(rs.getInt("userIdx"));
            review.setUserName(rs.getString("name"));
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