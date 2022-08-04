package umc.dosports.User;

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
    public Long join(User user) {
        return userRepository.save(user);
    }

    public Long login(String email, String passwd) {
        return userRepository.login(email, passwd);
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByName(user.getName())
                .ifPresent(m -> { //ifPresent는 null이 아닌 어떤 값이 있어야 로직이 동작
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                }); //result가 Optional이기 때문에 가능하다
    }

    /**
     * 전체 회원 조회
     */
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * 회원 삭제
     */
    public String deleteUser(Long id) {
        return userRepository.delete(id);
    }
}
