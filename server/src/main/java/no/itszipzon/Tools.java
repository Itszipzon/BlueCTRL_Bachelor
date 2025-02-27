package no.itszipzon;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Class for tools that are used in the backend.
 */
public class Tools {
  private Tools() {
  }

  public static Dotenv getEnv() {
    return Dotenv.load();
  }

  /**
   * Method to get the correct URL.
   *
   * @param url The URL to check
   * @return The correct URL
   */
  public static String getCorrectUrl(String url) {
    if (System.getProperty("os.name").contains("Windows")) {
      url = url.substring(1);
    }
    return url;
  }

  public static boolean matchPasswords(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
  }

  public static String hashPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    return encoder.encode(password);
  }

  public static Date formatDate(Date date) {
    return new Date(date.getTime());
  }
}
