package team.dankookie.server4983.book.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BookImage {

    @Id
    @GeneratedValue
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_book_id")
    private UsedBook usedBook;

    @Builder
    public BookImage(String imageUrl, UsedBook usedBook) {
        this.imageUrl = imageUrl;
        this.usedBook = usedBook;
    }
}
