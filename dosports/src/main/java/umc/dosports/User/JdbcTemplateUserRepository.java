package umc.dosports.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import umc.dosports.jwt.TokenProvider;
import umc.dosports.mail.MailHandler;
import umc.dosports.mail.TempKey;

import javax.mail.MessagingException;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class JdbcTemplateUserRepository implements UserRepository{

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    JavaMailSender mailSender;

    private final JdbcTemplate jdbcTemplate;
    private TokenProvider tokenProvider = new TokenProvider();
    private TempKey tempKey = new TempKey();

    public JdbcTemplateUserRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //회원가입
    @Override
    public Long save(User user) throws UnsupportedEncodingException, MessagingException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("userIdx");
        Map<String, Object> parameters = new HashMap<>();
        String userEmail = user.getEmail();
        parameters.put("email", userEmail);
        parameters.put("passwd", user.getPasswd());
        parameters.put("name", user.getName());
        parameters.put("gender", user.getGender());
        String mail_key = tempKey.getKey(30, false);
        parameters.put("mail_key", mail_key);
        parameters.put("mail_auth", 0);
        parameters.put("height", user.getHeight());
        parameters.put("weight", user.getWeight());
        parameters.put("profileImgPath", user.getProfileImgPath());

        //가입 인증 메일 발송 !!!추후 localhost 부분 도메인 명으로 변경해줘야 함!!!
        MailHandler sendMail = new MailHandler(mailSender);
        sendMail.setSubject("[DO SPORTS 인증 메일입니다.]"); //메일 제목
        sendMail.setText("<h1>DO SPORTS 메일 인증</h1>" +
                "<br>DO SPORTS에 가입하신 것을 환영합니다!" +
                "<br>아래 [이메일 인증 확인]을 누르시면 인증이 완료 됩니다." +
                "<br><a href='http://localhost:8080/user/confirmEmail?email=" + userEmail +
                "&mail_key=" + mail_key +
                "' target='_blank'>이메일 인증 확인</a>");
        sendMail.setFrom("doSportsHanda@gmail.com", "DOSPORTS");
        sendMail.setTo(userEmail);
        sendMail.send();

        Number key = jdbcInsert.executeAndReturnKey(new
                MapSqlParameterSource(parameters));

        return key.longValue();
    }

    @Override
    public void confirmEmail(String email, String mail_key) {
        //이메일로 유저 찾기
        Optional<User> user = findByEmail(email);
        if (mail_key.equals(user.get().getMail_key())) {
            //쿼리스트링으로 받은 mail_key와 DB에 저장된 mail_key가 일치한다면 해당 유저의 mail_auth 값을 1로 변경
            try {
                String sql = "update user set mail_auth=? where userIdx=?";
                jdbcTemplate.update(sql, 1, user.get().getIdx());
            } catch (EmptyResultDataAccessException e) {
                System.out.println("mail_auth 수정 실패");
            }
        }
    }

    @Override
    public void resetPasswd(String email) throws MessagingException, UnsupportedEncodingException {
        String resetPasswd = tempKey.getKey(10, false);
        updatePasswd(resetPasswd, email);

        MailHandler sendMail = new MailHandler(mailSender);
        sendMail.setSubject("[DO SPORTS 임시 비밀번호입니다.]"); //메일 제목
        sendMail.setText("<h1>로그인 후 반드시 새로운 비밀번호로 변경해주시길 바랍니다.</h1>" +
                "<h2>임시 비밀번호: " + resetPasswd + "</h2>");
        sendMail.setFrom("doSportsHanda@gmail.com", "DOSPORTS");
        sendMail.setTo(email);
        sendMail.send();
    }

    @Override
    public void updateUserInfo(Long idx, UserForm form) {
        Optional<User> user = findByIdx(idx);

        if (form.getName() != null) {
            try {
                String sql = "update user set name=? where userIdx=?";
                jdbcTemplate.update(sql, form.getName(), idx);
            } catch (EmptyResultDataAccessException e) {
                System.out.println("유저 닉네임 변경 실패");
            }
        }
        if (form.getPasswd() != null && passwordEncoder.matches(form.getPasswd(), user.get().getPasswd())) {
            updatePasswd(form.getNewPasswd(), user.get().getEmail());
        }
    }

    @Override
    public Object login(String email, String passwd) {
        try {
            // 이메일로 해당 유저 객체 찾음
            Optional<User> user = findByEmail(email);

            // 로그인 성공시(mail_auth 값이 true(1), 입력한 비밀번호와 DB의 비밀번호가 일치할 때)
            if (!user.get().isMail_auth()) {
                System.out.println("이메일 인증되지 않음");
                return Long.parseLong("-1");
            }
            else if (passwordEncoder.matches(passwd, user.get().getPasswd())) {
                Long userIdx = user.get().getIdx();
                // accessToken, refreshToken 생성
                String accessToken = tokenProvider.createAccessToken(userIdx);
                String refreshToken = tokenProvider.createRefreshToken();
                // DB에 refreshToken 저장
                updateRefreshToken(userIdx, refreshToken);

                // accessToken, refreshToken 반환
                Map<String, Object> result = new HashMap<>();
                result.put("accessToken", accessToken);
                result.put("refreshToken", refreshToken);

                return result;
            } else {
                System.out.println("비밀번호가 틀림");
                return Long.parseLong("-2");
            }
        } catch (NoSuchElementException e) {
            System.out.println("해당 객체 존재하지 않음");
            return Long.parseLong("-3");
        }
    }

    @Override
    public Object updateAccessToken(Long userIdx, String refreshToken) {
        //만약 클라이언트가 넘겨준 refreshToken이 DB에 존재하던 refreshToken과 일치한다면
        //새로운 accessToken 발급, 새로운 refreshToken 발급 후 DB 업데이트
        if (refreshToken.equals(findRefreshToken(userIdx))) {
            // accessToken, newRefreshToken 생성
            String accessToken = tokenProvider.createAccessToken(userIdx);
            String newRefreshToken = tokenProvider.createRefreshToken();
            // DB에 newRefreshToken 저장
            updateRefreshToken(userIdx, newRefreshToken);

            // accessToken, newRefreshToken 반환
            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", accessToken);
            result.put("refreshToken", newRefreshToken);

            return result;
        }

        return null;
    }

    @Override
    public Optional<User> findByIdx(Long idx) {
        List<User> result = jdbcTemplate.query("select * from user where userIdx = ?", userRowMapper(), idx);
        return result.stream().findAny();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> result = jdbcTemplate.query("select * from user where email = ?", userRowMapper(), email);
        return result.stream().findAny();
    }

    @Override
    public Optional<User> findByName(String name) {
        List<User> result = jdbcTemplate.query("select * from user where name = ?", userRowMapper(), name);
        return result.stream().findAny();
    }

    public String findRefreshToken(Long userIdx) {
        List<User> result = jdbcTemplate.query("select * from user where userIdx = ?", userRowMapper(), userIdx);
        return result.stream().findAny().get().getRefreshToken();
    }

    public void updateRefreshToken(Long idx, String refreshToken) {
        try {
            String sql = "update user set refreshToken = ? where userIdx = ?";
            jdbcTemplate.update(sql, refreshToken, idx);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("리프레쉬 토큰 저장 DB 오류");
        }
    }

    @Override
    public void updateUserFilterInfo(Long idx, UserForm form) {
        try {
            String sql = "update user set height=?, weight=? where userIdx=?";
            jdbcTemplate.update(sql, form.getHeight(), form.getWeight(), idx);
        } catch (EmptyResultDataAccessException e) {
        }
    }

    public void updatePasswd(String passwd, String email) {
        try {
            String sql = "update user set passwd=? where email=?";
            jdbcTemplate.update(sql, passwordEncoder.encode(passwd), email);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("비밀번호 변경 실패");
        }
    }

    @Override
    public String delete(Long idx) {
        try {
            jdbcTemplate.update("delete from user where userIdx = ?", idx);
            return "성공적으로 삭제되었습니다.";
        } catch (EmptyResultDataAccessException e) {
            return "유저 삭제에 실패했습니다.";
        }
    }

    @Override
    public void updateProfileImg(Long idx, String path) {
        try {
            String sql = "update user set profileImgPath=? where userIdx=?";
            jdbcTemplate.update(sql, path, idx);
        } catch (EmptyResultDataAccessException e) {
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select * from user", userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setIdx(rs.getLong("userIdx"));
            user.setName(rs.getString("name"));
            user.setPasswd(rs.getString("passwd"));
            user.setEmail(rs.getString("email"));
            user.setGender(rs.getString("gender"));
            user.setHeight(rs.getInt("height"));
            user.setWeight(rs.getInt("weight"));
            user.setMail_key(rs.getString("mail_key"));
            user.setMail_auth(rs.getBoolean("mail_auth"));
            user.setProfileImgPath(rs.getString("profileImgPath"));
            user.setRefreshToken(rs.getString("refreshToken"));
            user.setRegDate(rs.getString("regDate"));
            user.setUpdateDate(rs.getString("updateDate"));
            return user;
        };
    }
}