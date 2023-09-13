package team.dankookie.server4983.book.dto;

public record UsedBookSaveResponse(
        Long usedBookId
) {

    public static UsedBookSaveResponse of(Long usedBookId) {
        return new UsedBookSaveResponse(usedBookId);
    }
}
