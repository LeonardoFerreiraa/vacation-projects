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
    private final HttpStatus status = HttpStatus.OK;

    private final T body;

    private final URI location;

    public static ResponseEntity<?> created(final URI location) {
        return builder()
                .status(HttpStatus.CREATED)
                .location(location)
                .build();
    }

}
