package no.itszipzon.api;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Hello

/**
 * The API class is a controller class that handles default api requests.
 */
@RestController
@RequestMapping("/api")
public class Api {

  /**
   * Returns the default profile picture.
   *
   * @return The default profile picture.
   */
  @GetMapping("/default/pfp")
  public ResponseEntity<Resource> getProfilePicture() {
    return null;
  }

}
