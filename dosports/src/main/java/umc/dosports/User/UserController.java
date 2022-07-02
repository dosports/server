package umc.dosports.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;
    @Autowired
    private SecurityService securityService;

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
        user.setPasswd(form.getPasswd());
        user.setName(form.getName());

        long userIdx = userService.join(user);
        Map<String, Long> result = new HashMap<>();
        result.put("userIdx", userIdx);

        return result;
    }

    /*모든user조회*/
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
        Map<String, Long> result = new HashMap<>();
        result.put("userIdx", userIdx);

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
    /*마이페이지userinfo페이지*/
    @GetMapping("/user/info")
    public String createInfoForm() {
        return "users/createUserInfoForm";
    }
}
