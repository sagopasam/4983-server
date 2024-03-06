package team.dankookie.server4983.member.service;

import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService, Serializable {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
