package team.dankookie.server4983.book.repository.usedBook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.dto.AdminUsedBookListResponse;
import team.dankookie.server4983.book.dto.UsedBookListResponse;

import java.util.List;

public interface UsedBookRepositoryCustom {

    Page<AdminUsedBookListResponse> getAdminUsedBookList(Pageable pageable, String searchKeyword, BookStatus bookStatus);


    List<UsedBookListResponse> getUsedBookList(boolean isOrderByTradeAvailableDatetime);

    List<UsedBookListResponse> getUsedBookListInCollegeAndDepartment(List<College> college, List<Department> department, boolean isOrderByTradeAvailableDatetime);

    List<UsedBookListResponse> getUsedBookListBySearchKeyword(String searchKeyword, boolean isOrderByTradeAvailableDatetime);
}
