package team.dankookie.server4983.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.dto.UsedBookListResponse;
import team.dankookie.server4983.book.service.UsedBookListService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/used-book-list")
public class UsedBookListController {

    private final UsedBookListService usedBookListService;

    @GetMapping
    public ResponseEntity<List<UsedBookListResponse>> getUsedBookList(@RequestParam boolean canBuyElseAll) {
        return ResponseEntity.ok(usedBookListService.getUsedBookList(canBuyElseAll));
    }

    @GetMapping("/college-and-department")
    public ResponseEntity<List<UsedBookListResponse>> getUsedBookListByCollegeAndDepartment(
            @RequestParam boolean canBuyElseAll,
            @RequestParam List<College> college,
            @RequestParam List<Department> department
    ) {
        return ResponseEntity.ok(usedBookListService.getUsedBookList(college, department, canBuyElseAll));
    }


}
