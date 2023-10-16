package team.dankookie.server4983.book.repository.usedBook;

import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.dto.UsedBookListResponse;

import java.util.List;

public interface UsedBookRepositoryCustom {

    List<UsedBookListResponse> getUsedBookList(boolean isOrderByTradeAvailableDatetime);

    List<UsedBookListResponse> getUsedBookListInCollegeAndDepartment(List<College> college, List<Department> department, boolean isOrderByTradeAvailableDatetime);

    List<UsedBookListResponse> getUsedBookListBySearchKeyword(String searchKeyword, boolean isOrderByTradeAvailableDatetime);
}
