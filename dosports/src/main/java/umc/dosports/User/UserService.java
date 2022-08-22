package umc.dosports.User;


import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * 회원 가입
     */
    public Long join(User user) throws UnsupportedEncodingException, MessagingException {
        validateDuplicateUser(user); //중복 회원 가입 방지
        return userRepository.save(user);
    }

    public Object login(String email, String passwd) {
        return userRepository.login(email, passwd);
    }

    public Object updateAccessToken(Long userIdx, String refreshToken) {
        return userRepository.updateAccessToken(userIdx, refreshToken);
    }

    //유저 이메일 확인
    public void confirmEmail(String email, String mail_key) {
        userRepository.confirmEmail(email, mail_key);
    }

    //유저 비밀번호 재발급 (임시 비밀번호)
    public void resetPasswd(String email) throws UnsupportedEncodingException, MessagingException {
        userRepository.resetPasswd(email);
    }

    //유저 닉네임, 비밀번호 수정
    public void updateUserInfo(Long idx, UserForm form) {
        userRepository.updateUserInfo(idx, form);
    }

    //유저 필터 정보 수정 (키, 몸무게)
    public void updateUserFilterInfo(Long idx, UserForm form) {
        userRepository.updateUserFilterInfo(idx, form);
    }

    //이메일 중복 가입 방지
    private void validateDuplicateUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m -> { //ifPresent는 null이 아닌 어떤 값이 있어야 로직이 동작
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                }); //result가 Optional이기 때문에 가능하다
    }

    public void updateProfileImg(Long idx, String path) {
        userRepository.updateProfileImg(idx, path);
    }

    /**
     * 전체 회원 조회
     */
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long userIdx) {
        return userRepository.findByIdx(userIdx);
    }

    /**
     * 회원 삭제
     */
    public String deleteUser(Long idx) {
        return userRepository.delete(idx);
    }

}
