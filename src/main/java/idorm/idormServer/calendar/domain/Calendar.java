package idorm.idormServer.calendar.domain;

import idorm.idormServer.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar extends BaseEntity {

    @Id
    @Column(name = "calendar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isDorm1Yn;
    private Boolean isDorm2Yn;
    private Boolean isDorm3Yn;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String content;
    private String location;
    private String url;

    @Builder
    public Calendar(Boolean isDorm1Yn,
                    Boolean isDorm2Yn,
                    Boolean isDorm3Yn,
                    LocalDate startDate,
                    LocalDate endDate,
                    LocalTime startTime,
                    LocalTime endTime,
                    String content,
                    String location,
                    String url) {
        this.isDorm1Yn = isDorm1Yn;
        this.isDorm2Yn = isDorm2Yn;
        this.isDorm3Yn = isDorm3Yn;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
        this.location = location;
        this.url = url;

        this.setIsDeleted(false);
    }

    public void delete() {
        this.setIsDeleted(true);
    }
}
