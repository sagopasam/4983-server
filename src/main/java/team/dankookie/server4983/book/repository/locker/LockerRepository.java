package team.dankookie.server4983.book.repository.locker;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.book.domain.Locker;
import team.dankookie.server4983.chat.domain.ChatRoom;

public interface LockerRepository extends JpaRepository<Locker, Long> {

    Locker findByChatRoom(ChatRoom chatRoom);

    Locker findByLockerNumberAAndIsExists(String lockerNumber , boolean isExists);

}
