package umc.dosports.User;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateUserRepository implements UserRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateUserRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long save(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("idx");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("passwd", user.getPasswd());
        parameters.put("name", user.getName());
        Number key = jdbcInsert.executeAndReturnKey(new
                MapSqlParameterSource(parameters));
        return key.longValue();
    }

    @Override
    public Long login(String email, String passwd) {
        try {
            String sql = "select idx from user where email = ? and passwd = ?";
            Object[] params = new Object[]{email, passwd};
            Long userIdx = jdbcTemplate.queryForObject(sql, params, Long.class);
            return userIdx;
        } catch (EmptyResultDataAccessException e) {
            return Long.parseLong("-1");
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> result = jdbcTemplate.query("select * from user where idx = ?", userRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public Optional<User> findByName(String name) {
        List<User> result = jdbcTemplate.query("select * from user where name = ?", userRowMapper(), name);
        return result.stream().findAny();
    }

    @Override
    public String update(Long id, User user) {
        return null;
    }

    @Override
    public String delete(Long id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select * from user", userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setIdx(rs.getLong("idx"));
            user.setName(rs.getString("name"));
            user.setPasswd(rs.getString("passwd"));
            user.setEmail(rs.getString("email"));
            return user;
        };
    }
}