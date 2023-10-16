package team.dankookie.server4983.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.book.dto.UsedBookListResponse;
import team.dankookie.server4983.book.repository.bookImage.BookImageRepository;
import team.dankookie.server4983.book.repository.mypageBookSalesDetail.BookSalesDetailRepository;
import team.dankookie.server4983.jwt.constants.TokenSecretKey;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPageBookSalesDetailListService {

    private final BookSalesDetailRepository bookSalesDetailRepository;
    private final BookImageRepository bookImageRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final TokenSecretKey tokenSecretKey;


    public List<UsedBookListResponse> getMyPageBookSalesDetailList(boolean canBuy, AccessToken accessToken) {
        String nickname = jwtTokenUtils.getNickname(accessToken.value(), tokenSecretKey.getSecretKey());

        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return bookSalesDetailRepository.getMyPageBookSalesDetailList(canBuy, member.getId());
    }
}
   /* public List<UsedBookListResponse> getMyPageBookSalesDetailList(boolean canBuy, AccessToken accessToken) {

        String nickname = jwtTokenUtils.getNickname(accessToken.value(), tokenSecretKey.getSecretKey());

        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));


        List<UsedBookListResponse> responseList = new ArrayList<>();
        List<UsedBook> mypageBookSalesDetailList = bookSalesDetailRepository.getMyPageBookSalesDetailList(canBuy, member.getId());
        return getUsedBookListResponse(responseList, mypageBookSalesDetailList);
    }

    private List<UsedBookListResponse> getUsedBookListResponse(List<UsedBookListResponse> responseList, List<UsedBook> mypageBookSalesDetailList) {
        for(UsedBook usedBook : mypageBookSalesDetailList){
            String firstImageUrl = bookImageRepository.getBookImageUrlByUsedBookId(usedBook.getId());
            UsedBookListResponse UsedBookListResponse = UsedBookListResponse.builder()
                    .imageUrl(firstImageUrl)
                    .bookStatus(usedBook.getBookStatus())
                    .name(usedBook.getName())
                    .tradeAvailableDatetime(usedBook.getTradeAvailableDatetime())
                    .createdAt(usedBook.getCreatedAt())
                    .price(usedBook.getPrice())
                    .build();
            responseList.add(UsedBookListResponse);
        }
        return responseList;
    }
}*/
