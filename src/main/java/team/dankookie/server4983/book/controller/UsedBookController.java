package team.dankookie.server4983.book.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.dankookie.server4983.book.dto.UsedBookResponse;
import team.dankookie.server4983.book.dto.UsedBookSaveRequest;
import team.dankookie.server4983.book.dto.UsedBookSaveResponse;
import team.dankookie.server4983.book.service.UsedBookService;
import team.dankookie.server4983.jwt.dto.AccessToken;

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
    ) throws ExecutionException, InterruptedException {
        UsedBookSaveResponse usedBookSaveResponse = usedBookService.saveAndSaveFiles(multipartFileList,
                usedBookSaveRequest, accessToken);

        return ResponseEntity.ok().body(usedBookSaveResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsedBookResponse> getUsedBook(@PathVariable Long id, AccessToken accessToken) {
        UsedBookResponse usedBookResponse = usedBookService.findByUsedBookId(id, accessToken.studentId());

        return ResponseEntity.ok().body(usedBookResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsedBook(@PathVariable Long id, AccessToken accessToken) {
        boolean isDeleted = usedBookService.deleteUsedBook(id, accessToken);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/image/{image}")
    public ResponseEntity<Void> deleteUsedBookImage(@PathVariable Long id, @PathVariable String image,
                                                    AccessToken accessToken) {
        boolean isDeleted = usedBookService.deleteUsedBookImage(id, image);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}")
    public ResponseEntity<UsedBookSaveResponse> updateUsedBook(
            @PathVariable Long id,
            @RequestPart(value = "fileList", required = false) List<MultipartFile> multipartFileList,
            @RequestPart(value = "usedBook") UsedBookSaveRequest usedBookSaveRequest,
            AccessToken accessToken
    ) throws ExecutionException, InterruptedException {
        UsedBookSaveResponse usedBookResponse = usedBookService.updateUsedBook(id, multipartFileList,
                usedBookSaveRequest, accessToken);

        return ResponseEntity.ok().body(usedBookResponse);
    }

}
