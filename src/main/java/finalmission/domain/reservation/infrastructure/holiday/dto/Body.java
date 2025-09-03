package finalmission.domain.reservation.infrastructure.holiday.dto;

public record Body(Items items,
                   String numOfRows,
                   String pageNo,
                   String totalCount) {
}
