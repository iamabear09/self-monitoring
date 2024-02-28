package monitoring.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    ENTITY_NOT_FOUND("존재하지 않는 ID 입니다.");

    private final String message;

}
