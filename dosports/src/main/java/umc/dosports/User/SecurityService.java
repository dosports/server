package umc.dosports.User;

public interface SecurityService {
    String createToken(String subject, long ttlMillis);

    String getSubject(String token);
}
