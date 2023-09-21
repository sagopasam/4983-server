package team.dankookie.server4983.book.repository.locker;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.book.domain.Locker;

public interface LockerRepository extends JpaRepository<Locker, Long> {
}
