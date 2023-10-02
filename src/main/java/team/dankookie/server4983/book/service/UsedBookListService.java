package team.dankookie.server4983.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.dto.UsedBookListResponse;
import team.dankookie.server4983.book.repository.bookImage.BookImageRepository;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UsedBookListService {

    private final UsedBookRepository usedBookRepository;
    private final BookImageRepository bookImageRepository;

    public List<UsedBookListResponse> getUsedBookList(boolean isOrderByTradeAvailableDatetime) {

        List<UsedBookListResponse> responseList = new ArrayList<>();

        List<UsedBook> usedBookList = usedBookRepository.getUsedBookList(isOrderByTradeAvailableDatetime);

        return getUsedBookListResponses(responseList, usedBookList);
    }

    public List<UsedBookListResponse> getUsedBookList(List<College> college, List<Department> department, boolean isOrderByTradeAvailableDatetime) {
        List<UsedBookListResponse> responseList = new ArrayList<>();

        List<UsedBook> usedBookList = usedBookRepository.getUsedBookListInCollegeAndDepartment(college, department, isOrderByTradeAvailableDatetime);

        return getUsedBookListResponses(responseList, usedBookList);
    }

    private List<UsedBookListResponse> getUsedBookListResponses(List<UsedBookListResponse> responseList, List<UsedBook> usedBookList) {
        for (UsedBook usedBook : usedBookList) {
            String firstImageUrl = bookImageRepository.getBookImageUrlByUsedBookId(usedBook.getId());
            UsedBookListResponse usedBookListResponse = UsedBookListResponse.builder()
                    .usedBookId(usedBook.getId())
                    .imageUrl(firstImageUrl)
                    .bookStatus(usedBook.getBookStatus())
                    .name(usedBook.getName())
                    .tradeAvailableDatetime(usedBook.getTradeAvailableDatetime())
                    .createdAt(usedBook.getCreatedAt())
                    .price(usedBook.getPrice())
                    .build();
            responseList.add(usedBookListResponse);
        }

        return responseList;
    }

    public List<UsedBookListResponse> getUsedBookListBySearchKeyword(String searchKeyword, boolean isOrderByTradeAvailableDatetime) {
        List<UsedBookListResponse> responseList = new ArrayList<>();

        List<UsedBook> usedBookList = usedBookRepository.getUsedBookListBySearchKeyword(searchKeyword, isOrderByTradeAvailableDatetime);
        return getUsedBookListResponses(responseList, usedBookList);
    }
}
