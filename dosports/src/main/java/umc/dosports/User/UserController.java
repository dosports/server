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


    @GetMapping("/users/new")
    public String createForm() {
        return "users/createUserForm";
    }

    @GetMapping("/user")
    @ResponseBody
    public List<User> getAllUser(){
        return userService.findUsers();
    }

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

    @DeleteMapping("/user/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable(name = "id") Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("security/generate/token")
    public Map<String, Object> generateToken(@RequestParam String subject) {
        String token = securityService.createToken(subject, 1000 * 60 * 60 * 24L);    // 24시간
        Map<String, Object> map = new HashMap<>();
        map.put("userid", subject);
        map.put("token", token);
        return map;
    }

    @GetMapping("security/get/subject")
    public String getSubject(@RequestParam String token) {
        String subject = securityService.getSubject(token);
        return subject;
    }
}
