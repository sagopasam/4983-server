package team.dankookie.server4983.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.AdminMemberListResponse;
import team.dankookie.server4983.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class AdminMemberService {

  private final MemberRepository memberRepository;

  public Member findMemberById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
  }

  public Page<AdminMemberListResponse> getMember(Pageable pageable, String searchKeyword) {

    return memberRepository.getMember(pageable, searchKeyword);
  }

  public Page<AdminMemberListResponse> getBlockedMember(Pageable pageable, String searchKeyword) {
    return memberRepository.getBlockedMember(pageable, searchKeyword);
  }

  @Transactional
  public boolean blockMember(Long id) {

    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    return member.updateBlocked();
  }
}
