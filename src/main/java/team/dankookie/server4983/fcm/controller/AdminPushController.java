package team.dankookie.server4983.fcm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.fcm.dto.AdminPushRequest;
import team.dankookie.server4983.fcm.service.PushService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/push")
public class AdminPushController {

    private final PushService pushService;

    @PostMapping
    public ResponseEntity<Void> push(@RequestBody AdminPushRequest request) {
        pushService.push(request);
        return ResponseEntity.ok().build();
    }
}
