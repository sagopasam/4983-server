package team.dankookie.server4983.fcm.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.dankookie.server4983.book.constant.Department;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AdminPushRequest {
    private List<Department> departments;
    private List<String> memberIds;
    private List<Integer> yearOfAdmissions;
    private String message;
    private boolean isAll;
}
