package team.dankookie.server4983.chat.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.dankookie.server4983.common.BaseRepositoryTest;
import team.dankookie.server4983.scheduler.constant.ScheduleType;
import team.dankookie.server4983.scheduler.entity.Schedule;
import team.dankookie.server4983.scheduler.repository.SchedulerRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchedulerRepositoryTest extends BaseRepositoryTest  {

    @Autowired
    SchedulerRepository schedulerRepository;

    @Test
    public void 특정시간에작동되는스케쥴러가져오기() {
        // given
        schedulerRepository.save(Schedule.builder().message("message1").time(LocalDateTime.of(2023 , 3 , 2 , 10 , 0 , 0)).build());
        schedulerRepository.save(Schedule.builder().message("message2").time(LocalDateTime.of(2023 , 3 , 2 , 10 , 10 , 0)).build());
        schedulerRepository.save(Schedule.builder().message("message3").time(LocalDateTime.of(2023 , 3 , 2 , 10 , 20 , 0)).build());
        schedulerRepository.save(Schedule.builder().message("message4").time(LocalDateTime.of(2023 , 3 , 2 , 10 , 20 , 0)).build());
        schedulerRepository.save(Schedule.builder().message("message5").time(LocalDateTime.of(2023 , 3 , 2 , 10 , 30 , 0)).build());
        schedulerRepository.save(Schedule.builder().message("message6").time(LocalDateTime.of(2023 , 3 , 2 , 10 , 40 , 0)).build());
        schedulerRepository.save(Schedule.builder().message("message7").time(LocalDateTime.of(2023 , 3 , 3 , 10 , 0 , 0)).build());
        schedulerRepository.save(Schedule.builder().message("message8").time(LocalDateTime.of(2023 , 3 , 3 , 10 , 10 , 0)).build());
        schedulerRepository.save(Schedule.builder().message("message9").time(LocalDateTime.of(2023 , 3 , 2 , 10 , 20 , 0)).build());
        schedulerRepository.save(Schedule.builder().message("message10").time(LocalDateTime.of(2023 , 3 , 3 , 10 , 30 , 0)).build());

        // when
        List<Schedule> result = schedulerRepository.findByAlertTime(LocalDateTime.of(2023 , 3 , 2 , 10 , 20 , 0));

        // then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getMessage()).isEqualTo("message3");
        assertThat(result.get(1).getMessage()).isEqualTo("message4");
        assertThat(result.get(2).getMessage()).isEqualTo("message9");
    }

}
