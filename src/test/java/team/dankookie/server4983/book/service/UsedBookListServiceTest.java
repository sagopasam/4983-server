package team.dankookie.server4983.book.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.dto.UsedBookListResponse;
import team.dankookie.server4983.book.repository.bookImage.BookImageRepository;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;
import team.dankookie.server4983.member.domain.Member;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class UsedBookListServiceTest {

    @InjectMocks
    UsedBookListService usedBookListService;

    @Mock
    UsedBookRepository usedBookRepository;

    @Mock
    BookImageRepository bookImageRepository;

    @Test
    void 판매중인_책의_리스트를_리턴한다() {
        //given
        final String imageUrl = "imageUrl";
        final boolean canBuy = false;

        final UsedBook book1 = createBook();
        final UsedBook book2 = createBook();

        when(usedBookRepository.getUsedBookList(canBuy))
                .thenReturn(List.of(book1, book2));
        when(bookImageRepository.getBookImageUrlByUsedBookId(any()))
                .thenReturn(imageUrl);
        //when
        List<UsedBookListResponse> usedBookList = usedBookListService.getUsedBookList(canBuy);

        //then
        assertThat(usedBookList).hasSize(2);
        assertThat(usedBookList.get(0).imageUrl()).isEqualTo(imageUrl);
        assertThat(usedBookList.get(1).imageUrl()).isEqualTo(imageUrl);
    }

    private UsedBook createBook() {
        return UsedBook.builder()
                .name("book")
                .price(10000)
                .bookStatus(BookStatus.SALE)
                .tradeAvailableDate(LocalDate.now())
                .buyerMember(Member.builder().build())
                .build();
    }



}