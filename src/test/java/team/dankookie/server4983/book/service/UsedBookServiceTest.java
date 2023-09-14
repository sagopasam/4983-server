package team.dankookie.server4983.book.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.BookImage;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.dto.UsedBookResponse;
import team.dankookie.server4983.book.dto.UsedBookSaveRequest;
import team.dankookie.server4983.book.dto.UsedBookSaveResponse;
import team.dankookie.server4983.book.repository.bookImage.BookImageRepository;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;
import team.dankookie.server4983.common.BaseServiceTest;
import team.dankookie.server4983.jwt.constants.TokenSecretKey;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.service.MemberService;
import team.dankookie.server4983.s3.dto.S3Response;
import team.dankookie.server4983.s3.service.S3UploadService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UsedBookServiceTest extends BaseServiceTest {

    @InjectMocks
    UsedBookService usedBookService;

    @Mock
    S3UploadService uploadService;

    @Mock
    UsedBookRepository usedBookRepository;

    @Mock
    BookImageRepository bookImageRepository;

    @Mock
    MemberService memberService;

    @Mock
    JwtTokenUtils jwtTokenUtils;

    @Mock
    TokenSecretKey tokenSecretKey;

    @Test
    void 중고책을_저장하고_중고책_관련_이미지들을_저장한다() {
        //given
        List<MultipartFile> multipartFileList = List.of(new MockMultipartFile("file", "fileOriginName", "image/jpeg", "file".getBytes()));
        UsedBookSaveRequest usedBookSaveRequest = UsedBookSaveRequest.of(
                College.LAW,
                Department.BUSINESS,
                15000,
                LocalDate.of(2023, 9, 13),
                "책이름",
                "출판사",
                false,
                true,
                true
        );
        final AccessToken accessToken = AccessToken.of("accessToken");
        final String nickname = "nickname";
        final Member member = Member.builder().build();
        final long usedBookId = 1L;

        when(jwtTokenUtils.getNickname(any(), any()))
                .thenReturn(nickname);
        when(memberService.findMemberByNickname(nickname))
                .thenReturn(member);
        when(usedBookRepository.save(any()))
                .thenReturn(UsedBook.builder().id(usedBookId).build());
        when(uploadService.saveFileWithUUID(any()))
                .thenReturn(S3Response.of("imageName", "fileS3Key", "fileOriginName"));

        //when
        UsedBookSaveResponse usedBookSaveResponse = usedBookService.saveAndSaveFiles(multipartFileList, usedBookSaveRequest, accessToken);

        //then
        Assertions.assertThat(usedBookSaveResponse.usedBookId()).isEqualTo(usedBookId);
    }

    @Test
    void 중고서적의_id값으로_중고서적을_찾는다() {
        //given
        final long usedBookId = 1L;
        String bookName = "책이름";
        String publisher = "출판사";
        final UsedBook usedBook = UsedBook.builder()
                .id(usedBookId)
                .college(College.LAW)
                .department(Department.BUSINESS)
                .sellerMember(Member.builder().build())
                .bookStatus(BookStatus.SALE)
                .buyerMember(Member.builder().build())
                .isCoverDamaged(true)
                .isDiscolorationAndDamage(true)
                .isUnderlinedOrWrite(true)
                .name(bookName)
                .publisher(publisher)
                .price(15000)
                .tradeAvailableDate(LocalDate.of(2023, 9, 13))
                .build();

        when(usedBookRepository.findById(usedBookId))
                .thenReturn(Optional.of(usedBook));
        when(bookImageRepository.findByUsedBook(usedBook))
                .thenReturn(List.of(BookImage.builder().build()));

        //when
        UsedBookResponse usedBookResponse = usedBookService.findByUsedBookId(usedBookId);

        //then
        Assertions.assertThat(usedBookResponse.getBookName()).isEqualTo(bookName);
        Assertions.assertThat(usedBookResponse.getPublisher()).isEqualTo(publisher);

    }




}