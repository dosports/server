package umc.dosports.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Long save(User user) throws UnsupportedEncodingException, MessagingException;
    Object login(String email, String passwd);
    void confirmEmail(String email, String mail_key);
    Optional<User> findByIdx(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    String delete(Long idx);
    List<User> findAll();
    void resetPasswd(String email) throws MessagingException, UnsupportedEncodingException;
    void updateUserInfo(Long idx, UserForm form);
    void updateUserFilterInfo(Long idx, UserForm form);
    void updateProfileImg(Long idx, String path);
    Object updateAccessToken(Long userIdx, String refreshToken);
}