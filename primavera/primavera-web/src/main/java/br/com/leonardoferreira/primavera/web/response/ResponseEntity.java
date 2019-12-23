package br.com.leonardoferreira.primavera.web.response;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseEntity<T> {

    @Builder.Default
    private final ResponseStatus status = ResponseStatus.OK;

    private final T body;

    private final URI location;

    public static ResponseEntity<?> notFound() {
        return builder()
                .status(ResponseStatus.NOT_FOUND)
                .build();
    }

    public static ResponseEntity<?> created(final URI location) {
        return builder()
                .status(ResponseStatus.CREATED)
                .location(location)
                .build();
    }

}
