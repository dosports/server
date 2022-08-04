package umc.dosports.Review;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import umc.dosports.Review.model.*;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class JdbcTemplateReviewRepository implements ReviewRepository{

    private final JdbcTemplate jdbcTemplate;


    public JdbcTemplateReviewRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    @Override//   public Long save(SetReviewRequest review) {return null;}
//
//        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
//        jdbcInsert.withTableName("review").usingGeneratedKeyColumns("idx");
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("userIdx", review.getUserIdx());
//        parameters.put("title", review.getTitle());
//        parameters.put("contents", review.getContents());
//        parameters.put("img_path", review.getImg_path());
//        Number key = jdbcInsert.executeAndReturnKey(new
//                MapSqlParameterSource(parameters));
//        return key.longValue();
//    }

    @Override
    public int createReview(SetReviewRequest review){
        String createReviewQuery = "insert into review (userIdx, title, img_path, img_path1, img_path2, img_path3, img_path4, " +
                "contents, rate, brand, sports, category, gender, height, weight, source, price, level)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createReviewParams = new Object[]{review.getUserIdx(), review.getTitle(), review.getImg_path().get(0)
                , review.getImg_path().get(1), review.getImg_path().get(2), review.getImg_path().get(3), review.getImg_path().get(4)
                , review.getContent(), review.getRate(), review.getBrand(), review.getSports(), review.getCategory(), review.getGender(),
                review.getHeight(), review.getWeight(), review.getSource(), review.getPrice(), review.getLevel()};
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public List<Integer> showReviews(String gender, String sports, Filter filter){   //category의 저장방식 수정!! 문자열로 변환시 오류 발생
        String findQuery = filter.makeFilterQuery();
        filter.printFilter();
        Object[] findQueryParam = new Object[]{gender, sports, (filter.getCategory().equals(""))?-1:filter.getCategory(),
                filter.getHeight()-5, filter.getHeight()+5, filter.getWeight()-5, filter.getWeight()+5, filter.getLevel(),
                filter.getMin_price(), filter.getMax_price()}; //문자열 부분이 빌 경우 필터가 제대로 작동을 안하여 if문을 통해 조정해주었다.
        for(int i=0; i<findQueryParam.length; i++){
            System.out.println(i+": "+findQueryParam[i].toString());
        }
        return jdbcTemplate.query(findQuery, (rs, rowNum) -> rs.getInt("reviewIdx"), findQueryParam);
    }

    public GetReviewRequest showReviewByIdx(int reviewIdx){
        String findQuery = "select r.reviewIdx, r.userIdx, u.name, r.img_path, r.brand, r.title, r.category, r.sports, r.rate, "+
                //"IF(likeCount is null, 0, likeCount) as likeCount, "+
                //"IF(commentCount is null, 0, commentCount) as commentCount, "+
                //"r.likes, r.comments, "+
                "r.gender, r.height, r.weight, r.level, r.source, r.price, r.content, r.regDate "+
                "from review as r " +
                "join user as u " +
                //"join (select l.reviewIdx, count(likeIdx) as likeCount from Like) l on l.reviewIdx = r.reviewIdx" +
                //"join (select c.reviewIdx, count(commentIdx) as commentCount from Comment) c on c.reviewIdx = r.reviewIdx" +
                "where r.reviewIdx = ?";
        long findQueryParam = reviewIdx;
        return jdbcTemplate.queryForObject(findQuery, reviewRowMapper(), findQueryParam);
    }

    public List<GetReviewRequest> showUserReview(int userIdx){
        String findQuery = "select r.reviewIdx, r.userIdx, u.name, r.img_path, r.brand, r.title, r.category, r.sports, r.rate, "+
                //"IF(likeCount is null, 0, likeCount) as likeCount, "+
                //"IF(commentCount is null, 0, commentCount) as commentCount, "+
                //"r.likes, r.comments, "+
                "r.gender, r.height, r.weight, r.level, r.source, r.price, r.content, r.regDate "+
                "from review as r " +
                "join user as u " +
                //"join (select l.reviewIdx, count(likeIdx) as likeCount from Like) l on l.reviewIdx = r.reviewIdx" +
                //"join (select c.reviewIdx, count(commentIdx) as commentCount from Comment) c on c.reviewIdx = r.reviewIdx" +
                "where r.userIdx = ?";
        long findQueryParam = userIdx;
        return jdbcTemplate.query(findQuery, reviewRowMapper(), findQueryParam);
    }

    private RowMapper<GetReviewRequest> reviewRowMapper() {
        return (rs, rowNum) -> {
            GetReviewRequest review = new GetReviewRequest();
            review.setReviewIdx(rs.getInt("reviewIdx"));
            review.setUserIdx(rs.getInt("userIdx"));
            review.setUserName(rs.getString("name"));
            review.setImg_path(rs.getString("img_path"));
            review.setBrand(rs.getString("brand"));
            review.setTitle(rs.getString("title"));
            review.setCategory(rs.getString("category"));
            review.setSports(rs.getString("sports"));
            review.setRate(rs.getInt("rate"));
            // review.setLikes(rs.getInt("likes"));
            // review.setComments(rs.getInt("comments"));
            review.setGender(rs.getString("gender").charAt(0));
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

    @Override
    public int updateReview(int reviewIdx, String content) {
        String modifyReviewQuery = "update review set content = ? where reviewIdx = ? ";
        Object[] modifyReviewParams = new Object[]{content, reviewIdx};
        return this.jdbcTemplate.update(modifyReviewQuery,modifyReviewParams);
    }

    @Override
    public GetReviewRequest deleteReview(GetReviewRequest getReviewRequest, int reviewIdx) {
        this.jdbcTemplate.update("delete from review where reviewIdx = ?",reviewIdx);
        return getReviewRequest;
    }

    public boolean checkReviewExists(int reviewIdx){
        String checkReviewExistQuery = "select exists(select reviewIdx from review where reviewIdx = ?)";
        int checkReviewExistParams = reviewIdx;
        return this.jdbcTemplate.queryForObject(checkReviewExistQuery,
                boolean.class,
                checkReviewExistParams);
    }
}

//    @Override
//    public List<Review> findAll() {
//        return jdbcTemplate.query("select * from review join user on review.userIdx = user.idx", postRowMapper());
//    }
//    private RowMapper<Review> postRowMapper() {
//        return (rs, rowNum) -> {
//            Review review = new Review();
//            review.setUserIdx(rs.getLong("userIdx"));
//            review.setTitle(rs.getString("title"));
//            review.setContents(rs.getString("contents"));
//            review.setImg_path(rs.getString("img_path"));
//            post.setUserName(rs.getString("name"));
//            return review;
//        };
//    }