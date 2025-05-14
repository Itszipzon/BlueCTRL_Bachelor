package no.itszipzon;

import io.github.cdimascio.dotenv.Dotenv;

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
}
