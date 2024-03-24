package team.dankookie.server4983.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.dto.UsedBookListResponse;
import team.dankookie.server4983.book.repository.bookImage.BookImageRepository;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UsedBookListService {

    private final UsedBookRepository usedBookRepository;

    public List<UsedBookListResponse> getUsedBookList(boolean isOrderByTradeAvailableDatetime) {
        return usedBookRepository.getUsedBookList(isOrderByTradeAvailableDatetime);
    }

    public List<UsedBookListResponse> getUsedBookList(List<College> college, List<Department> department, boolean isOrderByTradeAvailableDatetime) {
        return usedBookRepository.getUsedBookListInCollegeAndDepartment(college, department, isOrderByTradeAvailableDatetime);
    }


    public List<UsedBookListResponse> getUsedBookListBySearchKeyword(String searchKeyword, boolean isOrderByTradeAvailableDatetime) {
        return usedBookRepository.getUsedBookListBySearchKeyword(searchKeyword, isOrderByTradeAvailableDatetime);
    }
}
