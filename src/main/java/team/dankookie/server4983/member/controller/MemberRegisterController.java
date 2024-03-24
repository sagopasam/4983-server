package team.dankookie.server4983.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.dankookie.server4983.common.dto.BaseMessageResponse;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.MemberRegisterRequest;
import team.dankookie.server4983.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/register")
public class MemberRegisterController {

    private final MemberService memberService;

    @GetMapping("/duplicate/studentId")
    public ResponseEntity<BaseMessageResponse> isStudentIdDuplicate(@RequestParam String studentId) {
        boolean isDuplicate = memberService.isStudentIdDuplicate(studentId);

        if (isDuplicate) {
            return ResponseEntity.badRequest().body(BaseMessageResponse.of("이미 가입된 학번 정보입니다."));
        } else {
            return ResponseEntity.ok().build();
        }

    }

    @GetMapping("/duplicate/nickname")
    public ResponseEntity<BaseMessageResponse> isNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = memberService.isNicknameDuplicate(nickname);

        if (isDuplicate) {
            return ResponseEntity.badRequest().body(BaseMessageResponse.of("사용 중인 닉네임이에요!"));
        } else {
            return ResponseEntity.ok(BaseMessageResponse.of("사용 가능한 닉네임입니다."));
        }
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody MemberRegisterRequest request) {
        memberService.register(request);
        return ResponseEntity.ok().build();
    }
}
