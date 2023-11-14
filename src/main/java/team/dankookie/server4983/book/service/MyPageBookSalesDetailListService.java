package team.dankookie.server4983.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.book.dto.UsedBookListResponse;
import team.dankookie.server4983.book.repository.mypageBookSalesDetail.BookSalesDetailRepository;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPageBookSalesDetailListService {

    private final BookSalesDetailRepository bookSalesDetailRepository;
    private final MemberRepository memberRepository;

    public List<UsedBookListResponse> getMyPageBookSalesDetailList(boolean canBuy, AccessToken accessToken) {
        String studentId = accessToken.studentId();

        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return bookSalesDetailRepository.getMyPageBookSalesDetailList(canBuy, member.getId());
    }
}
