package team.dankookie.server4983.member.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.AdminMemberListResponse;

public interface MemberRepositoryCustom {
  Page<AdminMemberListResponse> getMember(Pageable pageable, String searchKeyword);

  Page<AdminMemberListResponse> getBlockedMember(Pageable pageable, String searchKeyword);

  List<Member> findByPushCondition(List<String> memberIds, List<Department> departments, List<Integer> yearOfAdmissions);
}
