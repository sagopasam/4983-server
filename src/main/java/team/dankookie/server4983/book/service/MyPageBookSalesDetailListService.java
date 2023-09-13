package team.dankookie.server4983.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.dto.MyPageBookSalesDetailListResponse;
import team.dankookie.server4983.book.repository.bookImage.BookImageRepository;
import team.dankookie.server4983.book.repository.mypageBookSalesDetail.BookSalesDetailRepository;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPageBookSalesDetailListService {

    private final BookSalesDetailRepository bookSalesDetailRepository;
    private final BookImageRepository bookImageRepository;
    private final MemberRepository memberRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    public List<MyPageBookSalesDetailListResponse> getMyPageBookSalesDetailList(boolean canBuy, String accessToken) {

        String nickname = JwtTokenUtils.getNickname(accessToken, secretKey);

        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));


        List<MyPageBookSalesDetailListResponse> responseList = new ArrayList<>();
        List<UsedBook> mypageBookSalesDetailList = bookSalesDetailRepository.getMyPageBookSalesDetailList(canBuy, member.getId());
        return getMyPageBookSalesDetailListResponse(responseList, mypageBookSalesDetailList);
    }

    private List<MyPageBookSalesDetailListResponse> getMyPageBookSalesDetailListResponse(List<MyPageBookSalesDetailListResponse> responseList, List<UsedBook> mypageBookSalesDetailList) {
        for(UsedBook usedBook : mypageBookSalesDetailList){
            String firstImageUrl = bookImageRepository.getBookImageUrlByUsedBookId(usedBook.getId());
            MyPageBookSalesDetailListResponse myPageBookSalesDetailListResponse = MyPageBookSalesDetailListResponse.builder()
                    .imageUrl(firstImageUrl)
                    .bookStatus(usedBook.getBookStatus())
                    .name(usedBook.getName())
                    .tradeAbailableDate(usedBook.getTradeAvailableDate())
                    .createdAt(usedBook.getCreatedAt())
                    .price(usedBook.getPrice())
                    .build();
            responseList.add(myPageBookSalesDetailListResponse);
        }
        return responseList;
    }
}
