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

    public List<UsedBookListResponse> getUsedBookList(boolean canBuyElseAll) {

        List<UsedBookListResponse> responseList = new ArrayList<>();

        List<UsedBook> usedBookList = usedBookRepository.getUsedBookList(canBuyElseAll);

        return getUsedBookListResponses(responseList, usedBookList);
    }

    public List<UsedBookListResponse> getUsedBookList(List<College> college, List<Department> department, boolean canBuyElseAll) {
        List<UsedBookListResponse> responseList = new ArrayList<>();

        List<UsedBook> usedBookList = usedBookRepository.getUsedBookListInCollegeAndDepartment(college, department, canBuyElseAll);

        return getUsedBookListResponses(responseList, usedBookList);
    }

    private List<UsedBookListResponse> getUsedBookListResponses(List<UsedBookListResponse> responseList, List<UsedBook> usedBookList) {
        for (UsedBook usedBook : usedBookList) {
            String firstImageUrl = bookImageRepository.getBookImageUrlByUsedBookId(usedBook.getId());
            UsedBookListResponse usedBookListResponse = UsedBookListResponse.builder()
                    .imageUrl(firstImageUrl)
                    .bookStatus(usedBook.getBookStatus())
                    .name(usedBook.getName())
                    .tradeAvailableDate(usedBook.getTradeAvailableDate())
                    .createdAt(usedBook.getCreatedAt())
                    .price(usedBook.getPrice())
                    .build();
            responseList.add(usedBookListResponse);
        }

        return responseList;
    }
}
