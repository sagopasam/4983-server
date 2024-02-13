package team.dankookie.server4983.fcm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.fcm.dto.FcmTokenRequest;
import team.dankookie.server4983.fcm.service.FcmService;
import team.dankookie.server4983.jwt.dto.AccessToken;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/fcm")
public class FcmController {

  private final FcmService fcmService;

  @PostMapping
  public void updateFcmToken(AccessToken accessToken, @RequestBody FcmTokenRequest request) {
    fcmService.updateFcmToken(accessToken.studentId(), request.token());
  }

}
