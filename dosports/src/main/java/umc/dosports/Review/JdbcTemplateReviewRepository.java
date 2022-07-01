package umc.dosports.Review;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateReviewRepository implements ReviewRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateReviewRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long save(Review review) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("review").usingGeneratedKeyColumns("idx");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userIdx", review.getUserIdx());
        parameters.put("title", review.getTitle());
        parameters.put("contents", review.getContents());
        parameters.put("img_path", review.getImg_path());
        Number key = jdbcInsert.executeAndReturnKey(new
                MapSqlParameterSource(parameters));
        return key.longValue();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Review> findByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public String update(Long id, Review review) {
        return null;
    }

    @Override
    public String delete(Long id) {
        return null;
    }

    @Override
    public List<Review> findAll() {
        return jdbcTemplate.query("select * from review join user on review.userIdx = user.idx", postRowMapper());
    }

    private RowMapper<Review> postRowMapper() {
        return (rs, rowNum) -> {
            Review review = new Review();
            review.setIdx(rs.getLong("idx"));
            review.setTitle(rs.getString("title"));
            review.setContents(rs.getString("contents"));
            review.setImg_path(rs.getString("img_path"));
            review.setUserIdx(rs.getLong("userIdx"));
//            post.setUserName(rs.getString("name"));
            return review;
        };
    }
}
