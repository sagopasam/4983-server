package team.dankookie.server4983.book.repository.usedBook;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.common.BaseRepositoryTest;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class UsedBookRepositoryTest extends BaseRepositoryTest {

    @Autowired
    UsedBookRepository usedBookRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 모든_서적을_리턴한다() {
        //given
        final boolean canBuyElseAll = false;

        Member member = Member.builder()
                .accountBank(AccountBank.K)
                .department(Department.DEPARTMENT_OF_LAW)
                .accountHolder("홍길동")
                .phoneNumber("010-1234-5678")
                .password("password")
                .nickname("nickname")
                .yearOfAdmission(2022)
                .studentId("20151234")
                .accountNumber("123123123")
                .build();

        memberRepository.save(member);

        UsedBook usedBookSale = UsedBook.builder()
                .bookStatus(BookStatus.SALE)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .price(10000)
                .name("책이름")
                .build();

        UsedBook usedBookTrade = UsedBook.builder()
                .bookStatus(BookStatus.TRADE)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .price(10000)
                .name("책이름")
                .build();

        UsedBook usedBookSold = UsedBook.builder()
                .bookStatus(BookStatus.SOLD)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .price(10000)
                .name("책이름")
                .build();



        usedBookRepository.save(usedBookSold);
        usedBookRepository.save(usedBookTrade);
        usedBookRepository.save(usedBookSale);


        //when
        List<UsedBook> usedBookList = usedBookRepository.getUsedBookList(canBuyElseAll);

        //then
        assertThat(usedBookList).hasSize(3);
        assertThat(usedBookList.get(0).getBookStatus()).isEqualTo(BookStatus.SALE);
        assertThat(usedBookList.get(1).getBookStatus()).isEqualTo(BookStatus.TRADE);
        assertThat(usedBookList.get(2).getBookStatus()).isEqualTo(BookStatus.SOLD);
    }

    @Test
    void 서적을_단과대와_학과에_따라_리턴한다() {
        //given
        final boolean canBuyElseAll = false;
        final List<Department> departmentList = List.of(Department.DEPARTMENT_OF_LAW);
        final List<College> collegeList = List.of(College.LAW);


        Member member = Member.builder()
                .department(Department.DEPARTMENT_OF_LAW)
                .accountBank(AccountBank.K)
                .accountHolder("홍길동")
                .phoneNumber("010-1234-5678")
                .password("password")
                .nickname("nickname")
                .yearOfAdmission(2022)
                .studentId("20151234")
                .accountNumber("123123123")
                .build();

        memberRepository.save(member);

        UsedBook usedBookWantToFind1 = UsedBook.builder()
                .college(collegeList.get(0))
                .department(departmentList.get(0))
                .bookStatus(BookStatus.SALE)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .price(10000)
                .name("책이름")
                .build();

        UsedBook usedBookWantToFind2 = UsedBook.builder()
                .college(collegeList.get(0))
                .department(departmentList.get(0))
                .bookStatus(BookStatus.SALE)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .price(10000)
                .name("책이름")
                .build();

        UsedBook usedBookElse = UsedBook.builder()
                .college(College.BUSINESS_AND_ECONOMICS)
                .department(Department.ACCOUNTING)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .bookStatus(BookStatus.SALE)
                .price(10000)
                .name("책이름")
                .build();


        usedBookRepository.save(usedBookWantToFind1);
        usedBookRepository.save(usedBookWantToFind2);
        usedBookRepository.save(usedBookElse);


        //when
        List<UsedBook> usedBookList = usedBookRepository.getUsedBookListInCollegeAndDepartment(collegeList, departmentList, canBuyElseAll);

        //then
        assertThat(usedBookList).hasSize(2);
    }

    @Test
    void 서적을_단과대에_따라_리턴한다() {
        //given
        final boolean canBuyElseAll = false;
        final List<Department> departmentList = List.of(Department.DEPARTMENT_OF_LAW);
        final List<College> collegeList = List.of(College.LAW);


        Member member = Member.builder()
                .department(Department.DEPARTMENT_OF_LAW)
                .accountBank(AccountBank.K)
                .accountHolder("홍길동")
                .phoneNumber("010-1234-5678")
                .password("password")
                .nickname("nickname")
                .yearOfAdmission(2022)
                .studentId("20151234")
                .accountNumber("123123123")
                .build();

        memberRepository.save(member);

        UsedBook usedBookWantToFind1 = UsedBook.builder()
                .college(collegeList.get(0))
                .department(departmentList.get(0))
                .bookStatus(BookStatus.SALE)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .price(10000)
                .name("책이름")
                .build();

        UsedBook usedBookWantToFind2 = UsedBook.builder()
                .college(collegeList.get(0))
                .department(departmentList.get(0))
                .bookStatus(BookStatus.SALE)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .price(10000)
                .name("책이름")
                .build();

        UsedBook usedBookElse = UsedBook.builder()
                .college(College.BUSINESS_AND_ECONOMICS)
                .department(Department.ACCOUNTING)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .bookStatus(BookStatus.SALE)
                .price(10000)
                .name("책이름")
                .build();


        usedBookRepository.save(usedBookWantToFind1);
        usedBookRepository.save(usedBookWantToFind2);
        usedBookRepository.save(usedBookElse);


        //when
        List<UsedBook> usedBookList = usedBookRepository.getUsedBookListInCollegeAndDepartment(collegeList, List.of(), canBuyElseAll);

        //then
        assertThat(usedBookList).hasSize(2);
    }

    @Test
    void 서적을_학과에_따라_리턴한다() {
        //given
        final boolean canBuyElseAll = false;
        final List<Department> departmentList = List.of(Department.DEPARTMENT_OF_LAW);
        final List<College> collegeList = List.of(College.LAW);


        Member member = Member.builder()
                .department(Department.DEPARTMENT_OF_LAW)
                .accountBank(AccountBank.K)
                .accountHolder("홍길동")
                .phoneNumber("010-1234-5678")
                .password("password")
                .nickname("nickname")
                .yearOfAdmission(2022)
                .studentId("20151234")
                .accountNumber("123123123")
                .build();

        memberRepository.save(member);

        UsedBook usedBookWantToFind1 = UsedBook.builder()
                .college(collegeList.get(0))
                .department(departmentList.get(0))
                .bookStatus(BookStatus.SALE)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .price(10000)
                .name("책이름")
                .build();

        UsedBook usedBookWantToFind2 = UsedBook.builder()
                .college(collegeList.get(0))
                .department(departmentList.get(0))
                .bookStatus(BookStatus.SALE)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .price(10000)
                .name("책이름")
                .build();

        UsedBook usedBookElse = UsedBook.builder()
                .college(College.BUSINESS_AND_ECONOMICS)
                .department(Department.ACCOUNTING)
                .buyerMember(member)
                .tradeAvailableDate(LocalDate.now())
                .bookStatus(BookStatus.SALE)
                .price(10000)
                .name("책이름")
                .build();


        usedBookRepository.save(usedBookWantToFind1);
        usedBookRepository.save(usedBookWantToFind2);
        usedBookRepository.save(usedBookElse);


        //when
        List<UsedBook> usedBookList = usedBookRepository.getUsedBookListInCollegeAndDepartment(List.of(), departmentList, canBuyElseAll);

        //then
        assertThat(usedBookList).hasSize(2);
    }


}