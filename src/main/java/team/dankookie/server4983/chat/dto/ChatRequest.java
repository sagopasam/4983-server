package team.dankookie.server4983.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.dankookie.server4983.chat.constant.ContentType;

import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatRequest {

    private Map<String , Object> data;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private ContentType contentType;

    private String session;

}
