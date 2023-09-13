package team.dankookie.server4983.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.dankookie.server4983.book.dto.UsedBookResponse;
import team.dankookie.server4983.book.dto.UsedBookSaveRequest;
import team.dankookie.server4983.book.dto.UsedBookSaveResponse;
import team.dankookie.server4983.book.service.UsedBookService;
import team.dankookie.server4983.jwt.dto.AccessToken;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/used-book")
public class UsedBookController {

    private final UsedBookService usedBookService;

    @PostMapping
    public ResponseEntity<UsedBookSaveResponse> saveSaveUsedBook(
            @RequestPart(value = "fileList", required = false) List<MultipartFile> multipartFileList,
            @RequestPart(value = "usedBook") UsedBookSaveRequest usedBookSaveRequest,
            AccessToken accessToken
    ) {
        UsedBookSaveResponse usedBookSaveResponse = usedBookService.saveAndSaveFiles(multipartFileList, usedBookSaveRequest, accessToken);

        return ResponseEntity.ok().body(usedBookSaveResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsedBookResponse> getUsedBook(@PathVariable Long id) {
        UsedBookResponse usedBookResponse = usedBookService.findByUsedBookId(id);

        return ResponseEntity.ok().body(usedBookResponse);
    }

}
