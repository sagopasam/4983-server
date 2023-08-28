package team.dankookie.server4983.book.repository.usedBook;

import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.UsedBook;

import java.util.List;

public interface UsedBookRepositoryCustom {

    List<UsedBook> getUsedBookList(boolean canBuyElseAll);

    List<UsedBook> getUsedBookListInCollegeAndDepartment(List<College> college, List<Department> department, boolean canBuyElseAll);
}
