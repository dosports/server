package umc.dosports.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Long save(User user);
    Long login(String email, String passwd);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    String update(Long id, User user);
    String delete(Long id);
    List<User> findAll();
}