package team.dankookie.server4983.scheduler.repository;

import team.dankookie.server4983.scheduler.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomSchedulerRepository {

    List<Schedule> findByAlertTime(LocalDateTime time);

}
