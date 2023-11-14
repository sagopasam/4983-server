package team.dankookie.server4983.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.member.dto.MemberImageRequest;
import team.dankookie.server4983.member.dto.MemberProfileSaveRequest;
import team.dankookie.server4983.member.dto.MemberProfileSaveResponse;
import team.dankookie.server4983.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/my-pages")
public class MemberUpdateController {

    private final MemberService memberService;

    @PostMapping("/update")
    public ResponseEntity<MemberProfileSaveResponse> updateMemberProfile(
            @RequestPart(value = "fileImage", required = false) MultipartFile multipartFile,
            @RequestPart(value = "member") MemberProfileSaveRequest memberProfileSaveRequest,
            AccessToken accessToken
    ) {
        MemberProfileSaveResponse memberProfileSaveResponse = memberService.updateMemberProfile(multipartFile, memberProfileSaveRequest, accessToken);

        return ResponseEntity.ok().body(memberProfileSaveResponse);
    }

    @DeleteMapping("/delete/image")
    public ResponseEntity<Void> deleteMyPageProfileImage(@RequestBody MemberImageRequest memberImageRequest, AccessToken accessToken) {
    boolean isDeleted = memberService.deleteMyPageProfileImage(memberImageRequest, accessToken.nickname());
    return ResponseEntity.noContent().build();
    }
}
