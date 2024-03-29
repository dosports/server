package umc.dosports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import umc.dosports.Comment.CommentRepository;
import umc.dosports.Comment.CommentService;
import umc.dosports.Comment.JdbcTemplateCommentRepository;
import umc.dosports.Like.JdbcTemplateLikeRepository;
import umc.dosports.Like.LikeRepository;
import umc.dosports.Like.LikeService;
import umc.dosports.Notification.JdbcTemplateNotiRepository;
import umc.dosports.Notification.NotiRepository;
import umc.dosports.Notification.NotiService;
import umc.dosports.Review.JdbcTemplateReviewRepository;
import umc.dosports.Review.ReviewRepository;
import umc.dosports.Review.ReviewService;
import umc.dosports.User.JdbcTemplateUserRepository;
import umc.dosports.User.UserRepository;
import umc.dosports.User.UserService;
import umc.dosports.jwt.TokenProvider;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }
    @Bean
    public ReviewService reviewService() {
        return new ReviewService(reviewRepository());
    }
    @Bean
    public CommentService commentService() {
        return new CommentService(commentRepository());
    }
    @Bean
    public LikeService LikeService() {
        return new LikeService(likeRepository());
    }
    @Bean
    public NotiService NotiService() {
        return new NotiService(notiRepository());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public TokenProvider tokenProvider() { return new TokenProvider(); }

    @Bean
    public UserRepository userRepository() {
        return new JdbcTemplateUserRepository(dataSource);
    }
    @Bean
    public ReviewRepository reviewRepository() {
        return new JdbcTemplateReviewRepository(dataSource);
    }
    @Bean
    public CommentRepository commentRepository() { return new JdbcTemplateCommentRepository(dataSource); }
    @Bean
    public LikeRepository likeRepository() { return new JdbcTemplateLikeRepository(dataSource); }
    @Bean
    public NotiRepository notiRepository() { return new JdbcTemplateNotiRepository(dataSource); }
}