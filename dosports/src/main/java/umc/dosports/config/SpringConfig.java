package umc.dosports;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import umc.dosports.Review.JdbcTemplateReviewRepository;
import umc.dosports.Review.ReviewRepository;
import umc.dosports.Review.ReviewService;
import umc.dosports.User.JdbcTemplateUserRepository;
import umc.dosports.User.UserRepository;
import umc.dosports.User.UserService;

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
    public UserRepository userRepository() {
        return new JdbcTemplateUserRepository(dataSource);
    }
    @Bean
    public ReviewRepository reviewRepository() {
        return new JdbcTemplateReviewRepository(dataSource);
    }
}
