package team.dankookie.server4983.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequest {

    private long salesPost;

    // FIXME 테스트용 임시 데이터
    private String bookName;

}
