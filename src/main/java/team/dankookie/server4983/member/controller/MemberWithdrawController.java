package team.dankookie.server4983.member.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberWithdrawController {
    private final MemberService memberService;

    @GetMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam String nickname) {
        boolean isWithdraw = memberService.checkMemberAndWithdraw(nickname);
        if (isWithdraw) {
            return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("이미 탈퇴한 회원이거나 회원을 찾을 수 없습니다.");
        }
    }
}
