package umc.dosports.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import umc.dosports.jwt.TokenProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;
    private TokenProvider tokenProvider = new TokenProvider();
    @Autowired
    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private SecurityService securityService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*회원가입페이지*/
    @GetMapping("/users/new")
    public String createForm() {
        return "users/createUserForm";
    }

    /*회원가입form*/
    @PostMapping("/user")
    @ResponseBody
    public Object join(@RequestBody UserForm form) {
        User user = new User();
        user.setEmail(form.getEmail());
        user.setPasswd(passwordEncoder.encode(form.getPasswd()));
        user.setName(form.getName());
        user.setGender(form.getGender());

        long userIdx = userService.join(user);
        Map<String, Long> result = new HashMap<>();
        result.put("userIdx", userIdx);

        return result;
    }

    /*모든 user*/
    @GetMapping("/user")
    @ResponseBody
    public List<User> getAllUser(){
        return userService.findUsers();
    }

    /*로그인 페이지*/
    @GetMapping("/user/loginForm")
    public String login() {
        return "users/loginForm";
    }

    /*로그인 form*/
    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestBody UserForm form) {
        String email = form.getEmail();
        String passwd = form.getPasswd();

        long userIdx = userService.login(email, passwd);
        String token = tokenProvider.makeJwtToken(userIdx);
        Map<String, Object> result = new HashMap<>();
        result.put("userIdx", userIdx);
        result.put("token", token);

        return result;
    }

    /*회원 탈퇴*/
    @DeleteMapping("/user/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable(name = "id") Long id) {
        return userService.deleteUser(id);
    }

    /*마이페이지*/
    @GetMapping("/user/mypage/{idx}")
    public String mypage() {
        return "users/createForm";
    }
    /*마이페이지 userinfo 입력*/
    @GetMapping("/user/info")
    public String createInfoForm() {
        return "users/createUserInfoForm";
    }
}
