package team.dankookie.server4983.scheduler.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.dankookie.server4983.chat.constant.ContentType;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.scheduler.constant.ScheduleType;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Schedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    private long targetId;

    private LocalDateTime time;

    private String message;

    private ScheduleType scheduleType;
}
