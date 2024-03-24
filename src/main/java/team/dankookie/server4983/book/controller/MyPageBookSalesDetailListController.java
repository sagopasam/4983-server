package team.dankookie.server4983.book.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.book.dto.UsedBookListResponse;
import team.dankookie.server4983.book.service.MyPageBookSalesDetailListService;
import team.dankookie.server4983.jwt.dto.AccessToken;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/my-pages/book-sales-detail-list")
public class MyPageBookSalesDetailListController {
    private final MyPageBookSalesDetailListService myPageBookSalesDetailListService;

    @GetMapping
    public ResponseEntity<List<UsedBookListResponse>> getMyPageBookSalesDetailList(@RequestParam boolean canBuy, AccessToken accessToken) {
        return ResponseEntity.ok(myPageBookSalesDetailListService.getMyPageBookSalesDetailList(canBuy, accessToken));
    }
}
