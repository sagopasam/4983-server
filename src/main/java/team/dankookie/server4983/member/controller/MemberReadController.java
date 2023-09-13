package team.dankookie.server4983.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.member.dto.MemberCollegeAndDepartment;
import team.dankookie.server4983.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberReadController {

    private final MemberService memberService;

    @GetMapping("/college-department")
    public ResponseEntity<MemberCollegeAndDepartment> getCollegeAndDepartment(AccessToken accessToken) {
        MemberCollegeAndDepartment memberCollegeAndDepartment = memberService.findMemberCollegeAndDepartment(accessToken);

        return ResponseEntity.ok(memberCollegeAndDepartment);
    }
}
