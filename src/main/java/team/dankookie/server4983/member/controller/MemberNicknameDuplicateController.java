package team.dankookie.server4983.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.member.dto.NicknameDuplicateResponse;
import team.dankookie.server4983.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/my-pages/nicknames/duplicates")
public class MemberNicknameDuplicateController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<NicknameDuplicateResponse> checkNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = memberService.checkNicknameDuplicate(nickname);
        /**
         * {
         *     isDuplicate : true or false
         * }
         */
        return ResponseEntity.ok(NicknameDuplicateResponse.of(isDuplicate));
    }

}