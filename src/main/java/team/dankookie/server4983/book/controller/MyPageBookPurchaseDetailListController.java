package team.dankookie.server4983.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.dto.UsedBookListResponse;
import team.dankookie.server4983.book.service.MyPageBookPurchaseDetailListService;
import team.dankookie.server4983.jwt.dto.AccessToken;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/my-pages/book-purchase-detail-list")
public class MyPageBookPurchaseDetailListController {
    private final MyPageBookPurchaseDetailListService myPageBookPurchaseDetailListService;

    @GetMapping
    public ResponseEntity<List<UsedBookListResponse>> getMyPageBookPurchaseDetailList(@RequestParam BookStatus bookStatus, AccessToken accessToken) {

        return ResponseEntity.ok(myPageBookPurchaseDetailListService.getMyPageBookPurchaseDetailList(bookStatus, accessToken));
    }

}
