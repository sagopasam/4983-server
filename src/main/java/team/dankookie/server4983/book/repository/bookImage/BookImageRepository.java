package team.dankookie.server4983.book.repository.bookImage;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.book.domain.BookImage;
import team.dankookie.server4983.book.domain.UsedBook;

public interface BookImageRepository extends JpaRepository<BookImage, Long> , BookImageRepositoryCustom{
}
