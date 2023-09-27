package team.dankookie.server4983.member.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.common.exception.LoginFailedException;
import team.dankookie.server4983.jwt.constants.TokenSecretKey;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.LoginRequest;
import team.dankookie.server4983.member.dto.MemberCollegeAndDepartment;
import team.dankookie.server4983.member.dto.MemberPasswordChangeRequest;
import team.dankookie.server4983.member.dto.MemberRegisterRequest;
import team.dankookie.server4983.member.repository.MemberRepository;

import java.time.LocalDate;

import static team.dankookie.server4983.chat.domain.ChatRoom.buildChatRoom;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final TokenSecretKey tokenSecretKey;
    private final ChatRoomRepository chatRoomRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;
    private final UsedBookRepository usedBookRepository;

    @PostConstruct
    public void init() {
        Member testMember1 = Member.builder()
                .studentId("202023604")
                .department(Department.COMPUTER)
                .yearOfAdmission(2020)
                .nickname("닉네임1")
                .password(passwordEncoder.encode("1234"))
                .phoneNumber("01073352306")
                .accountHolder("박재완")
                .accountBank(AccountBank.KB)
                .accountNumber("12500104097324")
                .marketingAgree(true)
                .build();

        Member testMember2 = Member.builder()
                .studentId("test")
                .department(Department.COMPUTER)
                .yearOfAdmission(2020)
                .nickname("닉네임2")
                .password(passwordEncoder.encode("1234"))
                .phoneNumber("01073352306")
                .accountHolder("박박박")
                .accountBank(AccountBank.KB)
                .accountNumber("12500104097324")
                .marketingAgree(true)
                .build();


        memberRepository.save(testMember1);
        memberRepository.save(testMember2);

        UsedBook usedBook1 = UsedBook.builder()
                .bookStatus(BookStatus.SALE)
                .name("사회과학통계방법")
                .tradeAvailableDate(LocalDate.of(2023, 7, 29))
                .price(13000)
                .isUnderlinedOrWrite(false)
                .isCoverDamaged(true)
                .isDiscolorationAndDamage(true)
                .department(Department.COMMUNICATION_DESIGN)
                .college(College.MUSIC_AND_ARTS)
                .sellerMember(testMember1)
                .build();

        UsedBook usedBook2 = UsedBook.builder()
                .bookStatus(BookStatus.SALE)
                .name("생명과학 길라잡이")
                .tradeAvailableDate(LocalDate.of(2023, 8, 9))
                .price(9000)
                .isUnderlinedOrWrite(true)
                .isCoverDamaged(true)
                .isDiscolorationAndDamage(true)
                .department(Department.COMMUNICATION_DESIGN)
                .college(College.MUSIC_AND_ARTS)
                .sellerMember(testMember1)
                .build();

        UsedBook usedBook3 = UsedBook.builder()
                .bookStatus(BookStatus.SALE)
                .name("경영학 원론")
                .tradeAvailableDate(LocalDate.of(2023, 7, 29))
                .price(9000)
                .isUnderlinedOrWrite(false)
                .isCoverDamaged(false)
                .isDiscolorationAndDamage(false)
                .department(Department.BUSINESS)
                .college(College.BUSINESS_AND_ECONOMICS)
                .sellerMember(testMember2)
                .build();

        UsedBook usedBook4 = UsedBook.builder()
                .bookStatus(BookStatus.TRADE)
                .name("컴퓨터 개론")
                .tradeAvailableDate(LocalDate.of(2023, 7, 29))
                .price(14000)
                .isUnderlinedOrWrite(false)
                .isCoverDamaged(false)
                .isDiscolorationAndDamage(false)
                .department(Department.BUSINESS)
                .college(College.BUSINESS_AND_ECONOMICS)
                .sellerMember(testMember2)
                .build();

        UsedBook usedBook5 = UsedBook.builder()
                .bookStatus(BookStatus.SOLD)
                .name("일반 화학")
                .tradeAvailableDate(LocalDate.of(2023, 7, 29))
                .price(7000)
                .isUnderlinedOrWrite(true)
                .isCoverDamaged(true)
                .isDiscolorationAndDamage(true)
                .department(Department.CHEMICAL)
                .college(College.ENGINEERING)
                .sellerMember(testMember2)
                .build();

        UsedBook usedBook6 = UsedBook.builder()
                .bookStatus(BookStatus.SOLD)
                .name("일반 화학2")
                .tradeAvailableDate(LocalDate.of(2023, 7, 29))
                .price(70002)
                .isUnderlinedOrWrite(true)
                .isCoverDamaged(true)
                .isDiscolorationAndDamage(true)
                .department(Department.CHEMICAL)
                .college(College.ENGINEERING)
                .sellerMember(testMember2)
                .build();
        UsedBook usedBook7 = UsedBook.builder()
                .bookStatus(BookStatus.SOLD)
                .name("일반 화학3")
                .tradeAvailableDate(LocalDate.of(2023, 7, 29))
                .price(100)
                .isUnderlinedOrWrite(true)
                .isCoverDamaged(true)
                .isDiscolorationAndDamage(true)
                .department(Department.CHEMICAL)
                .college(College.ENGINEERING)
                .sellerMember(testMember2)
                .build();
        UsedBook usedBook8 = UsedBook.builder()
                .bookStatus(BookStatus.SOLD)
                .name("일반 화학4")
                .tradeAvailableDate(LocalDate.of(2023, 7, 29))
                .price(100)
                .isUnderlinedOrWrite(true)
                .isCoverDamaged(true)
                .isDiscolorationAndDamage(true)
                .department(Department.CHEMICAL)
                .college(College.ENGINEERING)
                .sellerMember(testMember2)
                .build();
        UsedBook usedBook9 = UsedBook.builder()
                .bookStatus(BookStatus.SOLD)
                .name("일반 화학5")
                .tradeAvailableDate(LocalDate.of(2023, 7, 29))
                .price(100)
                .isUnderlinedOrWrite(true)
                .isCoverDamaged(true)
                .isDiscolorationAndDamage(true)
                .department(Department.CHEMICAL)
                .college(College.ENGINEERING)
                .sellerMember(testMember2)
                .build();
        UsedBook usedBook10 = UsedBook.builder()
                .bookStatus(BookStatus.SOLD)
                .name("일반 화학6")
                .tradeAvailableDate(LocalDate.of(2023, 7, 29))
                .price(100)
                .isUnderlinedOrWrite(true)
                .isCoverDamaged(true)
                .isDiscolorationAndDamage(true)
                .department(Department.CHEMICAL)
                .college(College.ENGINEERING)
                .sellerMember(testMember2)
                .build();

        usedBookRepository.save(usedBook1);
        usedBookRepository.save(usedBook2);
        usedBookRepository.save(usedBook3);
        usedBookRepository.save(usedBook4);
        usedBookRepository.save(usedBook5);
        usedBookRepository.save(usedBook6);
        usedBookRepository.save(usedBook7);
        usedBookRepository.save(usedBook8);
        usedBookRepository.save(usedBook9);
        usedBookRepository.save(usedBook10);

        ChatRoom chatRoom_1 = buildChatRoom(testMember1 , testMember2 , usedBook1);
        ChatRoom chatRoom_2 = buildChatRoom(testMember2 , testMember1 , usedBook2);
        chatRoomRepository.save(chatRoom_1);
        chatRoomRepository.save(chatRoom_2);

    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    public Member findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    }

    public boolean login(LoginRequest loginRequest) {

        Member member = memberRepository.findByStudentId(loginRequest.studentId())
                .orElseThrow(() -> new LoginFailedException("존재하지 않는 학번입니다."));

        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new LoginFailedException("잘못된 비밀번호입니다!");
        }
        return true;
    }

    public Member findMemberNicknameByStudentId(String studentId) {
        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new LoginFailedException("존재하지 않는 학번입니다."));
        return member;
    }

    public boolean isMemberExistsByMemberPasswordRequest(String studentId, String phoneNumber) {

        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학번입니다."));

        if (!member.getPhoneNumber().equals(phoneNumber)) {
            throw new IllegalArgumentException("학번과 맞지 않는 휴대폰번호입니다.");
        }

        return true;
    }

    @Transactional
    public boolean changeMemberPassword(MemberPasswordChangeRequest request) {
        Member member = memberRepository.findByStudentId(request.studentId())
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 학번입니다.")
                );

        if (!member.getPhoneNumber().equals(request.phoneNumber())) {
            throw new IllegalArgumentException("학번과 맞지 않는 휴대폰번호입니다.");
        }

        member.changePassword(passwordEncoder.encode(request.password()));
        return true;
    }

    public boolean isStudentIdDuplicate(String studentId) {
        return memberRepository.existsByStudentId(studentId);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    public Member register(MemberRegisterRequest request) {

        String encodedPassword = passwordEncoder.encode(request.password());

        return memberRepository.save(request.toEntity(encodedPassword));
    }

    public Boolean checkNicknameDuplicate(String nickname) {
        return memberRepository.existsMemberByNickname(nickname);
    }

    public boolean isMemberPasswordMatch(String password, String accessToken) {
        String nickname = jwtTokenUtils.getNickname(accessToken, secretKey);

        Member findMember = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (passwordEncoder.matches(password, findMember.getPassword())) {
            return true;
        }else {
            return false;
        }
    }

    public MemberCollegeAndDepartment findMemberCollegeAndDepartment(AccessToken accessToken) {
        String nickname = jwtTokenUtils.getNickname(accessToken.value(), tokenSecretKey.getSecretKey());
        Member member = findMemberByNickname(nickname);

        return MemberCollegeAndDepartment.of(member.getDepartment());
    }

    @Transactional
    public boolean checkMemberAndWithdraw(String nickname){
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        if (!member.getIsWithdraw()) {
            member.withdraw();
        }
        return member.getIsWithdraw();
    }
    }

