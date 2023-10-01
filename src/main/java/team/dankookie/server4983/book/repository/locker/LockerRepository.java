package team.dankookie.server4983.book.repository.locker;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.book.domain.Locker;
import team.dankookie.server4983.chat.domain.ChatRoom;

import java.util.Optional;

public interface LockerRepository extends JpaRepository<Locker, Long> {

    Locker findByChatRoom(ChatRoom chatRoom);

    Optional<Locker> findByLockerNumber(String lockerNumber);

}
