package team.dankookie.server4983.book.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Locker {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private Integer lockerNumber;

    @NotNull
    private String password;

    @Column(columnDefinition = "boolean default false")
    private Boolean isExists;

    private LocalDate tradeDate;

}
