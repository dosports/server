package umc.dosports.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.dosports.jwt.TokenProvider;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
public class UserController {

    private final UserService userService;
    private TokenProvider tokenProvider = new TokenProvider();
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String UPLOAD_PATH = "C:\\Users\\user\\Desktop\\server\\dosports\\src\\main\\resources\\image";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*회원가입페이지*/
    @GetMapping("/users/new")
    public String createForm() {
        return "users/createUserForm";
    }

    //회원가입
    @PostMapping("/user/signUp")
    @ResponseBody
    public Object join(UserForm form) throws UnsupportedEncodingException, MessagingException {
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

    /*이메일 인증*/
    @GetMapping("/user/confirmEmail")
    @ResponseBody
    public String emailConfirm(@RequestParam(value = "email") String email, @RequestParam(value = "mail_key") String mail_key)
            throws Exception {
        userService.confirmEmail(email, mail_key);

        return "home"; //이메일 인증 완료 페이지 주소를 리턴할 예정
    }

    /*비밀번호 리셋(비밀번호를 잊어버리셨나요?)*/
    @PostMapping("/user/email/password")
    @ResponseBody
    public String resetPasswd(UserForm form) throws UnsupportedEncodingException, MessagingException {
        userService.resetPasswd(form.getEmail());
        return null; //비번 리셋한 이후 뜨는 페이지 리턴할 예정 (임시 비밀번호를 발급했으니 로그인 후 비밀번호 변경을 반드시 해주세요)
    }

    //마이페이지 설정(프로필, 사용자명, 비밀번호 변경)
    @PatchMapping("/user/mypage")
    @ResponseBody
    public String updateUserInfo(HttpServletRequest request, UserForm form){
        userService.updateUserInfo(Long.parseLong(String.valueOf(request.getAttribute("userIdx"))), form);
        return null; //http status code 넘겨줘야할 것 같은데...
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
    public Object login(UserForm form) {
        String email = form.getEmail();
        String passwd = form.getPasswd();

        return userService.login(email, passwd);
    }

    //access token 만료 직전 refresh token을 통한 access token 재발급 신청
    @PostMapping("/user/updateAccessToken")
    @ResponseBody
    public Object updateAccessToken(HttpServletRequest request, UserForm form) {
        String refreshToken = form.getRefreshToken();

        return userService.updateAccessToken(Long.parseLong(String.valueOf(request.getAttribute("userIdx"))),
                refreshToken);
    }

    //유저 마이페이지 정보 전달
    @GetMapping("user/{userIdx}")
    @ResponseBody
    public Object findOneUser(@PathVariable(name = "userIdx") Long idx) {
        User user = userService.findOne(idx).get();

        Map<String, Object> result = new HashMap<>();
        result.put("userIdx", user.getIdx());
        result.put("name", user.getName());
        result.put("email", user.getEmail());
        result.put("gender", user.getGender());
        result.put("height", user.getHeight());
        result.put("weight", user.getWeight());
        result.put("profileImgPath", user.getProfileImgPath());
        return result;
    }

    /*회원 탈퇴*/
    @DeleteMapping("/user/delete")
    @ResponseBody
    public String deleteUser(HttpServletRequest request) {
        return userService.deleteUser(Long.parseLong(String.valueOf(request.getAttribute("userIdx"))));
    }

    //유저 로그인 시 이름 표시 api
    @GetMapping("/user/name")
    @ResponseBody
    public String getUserName(HttpServletRequest request) {
        return userService.findOne(Long.parseLong(String.valueOf(request.getAttribute("userIdx")))).get().getName();
    }

    //유저 마이페이지 조회
    @GetMapping("user/info")
    @ResponseBody
    public String createInfoForm() {
        return "users/createUserInfoForm";
    }

    //마이페이지 설정(키, 몸무게 변경)
    @PatchMapping("/user/info")
    @ResponseBody
    public String updateUserFilterInfo(HttpServletRequest request, UserForm form){
        userService.updateUserFilterInfo(Long.parseLong(String.valueOf(request.getAttribute("userIdx"))), form);
        return null; //http status code 넘겨줘야할 것 같은데...
    }

    //프로필 이미지 변경(헤더 토큰 자동으로 받아옴, 파일만 넘겨주면 됨)
    @PostMapping(path="/user/profileImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public void updateProfileImg(HttpServletRequest request, @RequestPart("file") MultipartFile file) throws IOException {
        userService.updateProfileImg(Long.parseLong(String.valueOf(request.getAttribute("userIdx"))), saveFile(file));
    }
    //프로필 사진 업로드 함수
    private String saveFile(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String img_path = uuid + "_" + file.getOriginalFilename();

        File dest = new File(UPLOAD_PATH, file.getOriginalFilename());
        file.transferTo(dest);

        return img_path;
    }
}
