package team.dankookie.server4983.book.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.domain.Locker;
import team.dankookie.server4983.book.dto.AdminLockerListResponse;
import team.dankookie.server4983.book.repository.locker.LockerRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminLockerService {

    private final LockerRepository lockerRepository;


    public List<AdminLockerListResponse> getLocker(String searchKeyword,
                                                   Boolean isExists) {

        List<AdminLockerListResponse> queryList = lockerRepository.getAdminLockerList(
                searchKeyword);

        if (isExists) {
            return queryList;
        } else {
            List<AdminLockerListResponse> list = new ArrayList<>();
            for (int i = 1; i <= 32; i++) {
                list.add(AdminLockerListResponse.of(i, "", false, "", null));
            }

            List<Integer> isExistsLockerNumberList = queryList.stream().map(AdminLockerListResponse::getLockerNumber)
                    .toList();

            list.removeIf(locker -> isExistsLockerNumberList.contains(locker.getLockerNumber()));
            return list;
        }
    }

    @Transactional
    public void updateLocker(Integer lockerNumber, Boolean isExists) {

        if (isExists) {
            lockerRepository.save(
                    Locker.builder()
                            .lockerNumber(lockerNumber)
                            .password("임의설정비밀번호")
                            .tradeDate(LocalDate.now())
                            .isExists(true)
                            .build()
            );
        } else {
            Locker locker = lockerRepository.findByLockerNumberAndIsExists(lockerNumber);

            locker.releaseLocker();
        }

    }
}
