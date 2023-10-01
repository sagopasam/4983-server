package team.dankookie.server4983.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.dto.MyPageBookPurchaseDetailListResponse;
import team.dankookie.server4983.book.repository.bookImage.BookImageRepository;
import team.dankookie.server4983.book.repository.mypageBookPurchaseDetail.BookPurchaseDetailRepository;
import team.dankookie.server4983.jwt.constants.TokenSecretKey;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service

public class MyPageBookPurchaseDetailListService {
    private final BookPurchaseDetailRepository bookPurchaseDetailRepository;
    private final BookImageRepository bookImageRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final TokenSecretKey tokenSecretKey;

    public List<MyPageBookPurchaseDetailListResponse> getMyPageBookPurchaseDetailList(BookStatus bookStatus, AccessToken accessToken) {

        String nickname = jwtTokenUtils.getNickname(accessToken.value(), tokenSecretKey.getSecretKey());

        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));


        List<MyPageBookPurchaseDetailListResponse> responseList = new ArrayList<>();
        List<UsedBook> mypageBookPurchaseDetailList = bookPurchaseDetailRepository.getMyPageBookPurchaseDetailList(bookStatus, member.getId());
        return getMyPageBookPurchaseDetailListResponse(responseList, mypageBookPurchaseDetailList);
    }

    private List<MyPageBookPurchaseDetailListResponse> getMyPageBookPurchaseDetailListResponse(List<MyPageBookPurchaseDetailListResponse> responseList, List<UsedBook> mypageBookPurchaseDetailList) {
        for(UsedBook usedBook : mypageBookPurchaseDetailList){
            String firstImageUrl = bookImageRepository.getBookImageUrlByUsedBookId(usedBook.getId());
            MyPageBookPurchaseDetailListResponse myPageBookPurchaseDetailListResponse = MyPageBookPurchaseDetailListResponse.builder()
                    .imageUrl(firstImageUrl)
                    .bookStatus(usedBook.getBookStatus())
                    .name(usedBook.getName())
                    .tradeAvailableDatetime(usedBook.getTradeAvailableDatetime())
                    .createdAt(usedBook.getCreatedAt())
                    .price(usedBook.getPrice())
                    .build();
            responseList.add(myPageBookPurchaseDetailListResponse);
        }
        return responseList;
    }
}
