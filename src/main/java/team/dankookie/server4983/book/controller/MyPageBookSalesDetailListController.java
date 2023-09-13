package team.dankookie.server4983.book.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.book.dto.MyPageBookSalesDetailListResponse;
import team.dankookie.server4983.book.service.MyPageBookSalesDetailListService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/my-pages/book-sales-detail-list")
public class MyPageBookSalesDetailListController {
    private final MyPageBookSalesDetailListService myPageBookSalesDetailListService;

    @GetMapping
    public ResponseEntity<List<MyPageBookSalesDetailListResponse>> getMyPageBookSalesDetailList(@RequestParam boolean canBuy, HttpServletRequest request) {
//        FIXME :accessToken 코드 수정 필요
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);


        return ResponseEntity.ok(myPageBookSalesDetailListService.getMyPageBookSalesDetailList(canBuy, accessToken));
    }
}
