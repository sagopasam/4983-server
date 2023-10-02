package team.dankookie.server4983.book.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import team.dankookie.server4983.chat.domain.ChatRoom;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Locker {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    private ChatRoom chatRoom;

    @NotNull
    private String lockerNumber;

    @NotNull
    private String password;

    @Column(columnDefinition = "boolean default false")
    private Boolean isExists;

    private LocalDateTime tradeDate;

    public void releaseLocker() {
        isExists = false;
    }

}
