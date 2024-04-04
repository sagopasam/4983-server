package team.dankookie.server4983.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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

@SpringBootTest
class UsedBookServiceTest extends BaseServiceTest {

    @Autowired
    UsedBookService usedBookService;

    @Autowired
    ThreadPoolTaskExecutor defaultTaskExecutor;

    @MockBean
    S3UploadService uploadService;

    @MockBean
    UsedBookRepository usedBookRepository;

    @MockBean
    BookImageRepository bookImageRepository;

    @MockBean
    MemberService memberService;

    @MockBean
    JwtTokenUtils jwtTokenUtils;

    @MockBean
    TokenSecretKey tokenSecretKey;

    @Test
    @DisplayName("중고책과 관련 이미지를 저장한다")
    void saveBook() throws ExecutionException, InterruptedException {
        //given
        List<MultipartFile> multipartFileList = List.of(
                new MockMultipartFile("file", "fileOriginName", "image/jpeg", "file".getBytes()));
        UsedBookSaveRequest usedBookSaveRequest = UsedBookSaveRequest.of(
                College.LAW,
                Department.BUSINESS,
                15000,
                LocalDateTime.of(2023, 9, 13, 12, 0),
                "책이름",
                "출판사",
                false,
                true,
                true
        );
        final AccessToken accessToken = AccessToken.of("accessToken", "studentId");
        final String studentId = "studentId";
        final Member member = Member.builder().build();
        final long usedBookId = 1L;

        when(jwtTokenUtils.getStudentId(any()))
                .thenReturn(studentId);
        when(memberService.findMemberByStudentId(studentId))
                .thenReturn(member);
        when(usedBookRepository.save(any()))
                .thenReturn(UsedBook.builder().id(usedBookId).build());
        when(uploadService.saveFileWithUUID(any()))
                .thenReturn(S3Response.of("imageName", "fileS3Key", "fileOriginName"));

        //when
        UsedBookSaveResponse usedBookSaveResponse = usedBookService.saveAndSaveFiles(multipartFileList,
                usedBookSaveRequest, accessToken);

        //then
        assertThat(usedBookSaveResponse.usedBookId()).isEqualTo(usedBookId);
    }

    @Test
    @DisplayName("비동기로 중고책과 관련 이미지를 저장한다.")
    void saveBookByAsync() throws ExecutionException, InterruptedException {
        //given
        List<MultipartFile> multipartFileList = List.of(
                new MockMultipartFile("file", "fileOriginName", "image/jpeg", "file".getBytes()),
                new MockMultipartFile("file2", "fileOriginName2", "image/jpeg", "file".getBytes()));
        UsedBookSaveRequest usedBookSaveRequest = UsedBookSaveRequest.of(
                College.LAW,
                Department.BUSINESS,
                15000,
                LocalDateTime.of(2023, 9, 13, 12, 0),
                "책이름",
                "출판사",
                false,
                true,
                true
        );
        final AccessToken accessToken = AccessToken.of("accessToken", "studentId");
        final String studentId = "studentId";
        final Member member = Member.builder().build();
        final long usedBookId = 1L;

        when(jwtTokenUtils.getStudentId(any()))
                .thenReturn(studentId);
        when(memberService.findMemberByStudentId(studentId))
                .thenReturn(member);
        when(usedBookRepository.save(any()))
                .thenReturn(UsedBook.builder().id(usedBookId).build());
        when(uploadService.saveFileWithUUID(any()))
                .thenReturn(S3Response.of("imageName", "fileS3Key", "fileOriginName"));

        //when
        usedBookService.saveAndSaveFiles(multipartFileList,
                usedBookSaveRequest, accessToken);

        //then
        assertThat(defaultTaskExecutor.getActiveCount()).isEqualTo(2);
//        verify(defaultTaskExecutor, times(2)).execute(any());
    }


    @Test
    @DisplayName("id값으로 중고서적을 찾는다.")
    void findBook() {
        //given
        final String nickname = "studentId";
        final long usedBookId = 1L;
        final String bookName = "책이름";
        final String publisher = "출판사";
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
                .tradeAvailableDatetime(LocalDateTime.of(2023, 9, 13, 12, 0))
                .build();

        when(usedBookRepository.findById(usedBookId))
                .thenReturn(Optional.of(usedBook));
        when(bookImageRepository.findByUsedBook(usedBook))
                .thenReturn(List.of(BookImage.builder().build()));

        //when
        UsedBookResponse usedBookResponse = usedBookService.findByUsedBookId(usedBookId, nickname);

        //then
        assertThat(usedBookResponse.getBookName()).isEqualTo(bookName);
        assertThat(usedBookResponse.getPublisher()).isEqualTo(publisher);

    }

    @Test
    @DisplayName("중고서적을 삭제한다.")
    void removeBook() {
        //given
        Long usedBookId = 1L;
        String nickname = "studentId";
        Member member = Member.builder().build();

        when(jwtTokenUtils.getStudentId(any()))
                .thenReturn(nickname);
        when(memberService.findMemberByStudentId(nickname))
                .thenReturn(member);
        when(usedBookRepository.existsUsedBookByIdAndSellerMember(usedBookId, member))
                .thenReturn(true);
        when(usedBookRepository.findById(any()))
                .thenReturn(Optional.of(UsedBook.builder().build()));
        //when
        boolean isDeleted = usedBookService.deleteUsedBook(usedBookId, AccessToken.of("accessToken", nickname));

        //then
        assertThat(isDeleted).isTrue();
    }

    @Test
    @DisplayName("사용자가 다른 사용자 게시물을 삭제하는것에 실패한다.")
    void removeByOtherMember() {
        //given
        Long usedBookId = 1L;
        String nickname = "studentId";
        Member member = Member.builder().build();

        when(jwtTokenUtils.getStudentId(any()))
                .thenReturn(nickname);
        when(memberService.findMemberByStudentId(nickname))
                .thenReturn(member);
        when(usedBookRepository.existsUsedBookByIdAndSellerMember(usedBookId, member))
                .thenReturn(false);
        //when
        //then
        assertThatThrownBy(() -> {
            usedBookService.deleteUsedBook(usedBookId, AccessToken.of("accessToken", nickname));
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("글을 올린 사용자만 삭제할 수 있습니다.");
    }
}