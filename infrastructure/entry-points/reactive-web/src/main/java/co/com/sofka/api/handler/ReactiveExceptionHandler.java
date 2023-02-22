package co.com.sofka.api.handler;

import co.com.sofka.model.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReactiveExceptionHandler extends AbstractErrorWebExceptionHandler {

    public ReactiveExceptionHandler(ErrorAttributes errorAttributes, WebProperties webProperties, ApplicationContext applicationContext,
                                    ServerCodecConfigurer configurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable throwable = getError(request);

        ProblemDetail detalleProblema = obtenerDetalleProblema(throwable);
        detalleProblema.setInstance(URI.create(request.path()));
        detalleProblema.setType(URI.create(request.path()));

        log.error("Excepcion: {}, Response: {}", throwable, detalleProblema);
        return ServerResponse.status(detalleProblema.getStatus())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(BodyInserters.fromValue(detalleProblema));
    }

    private ProblemDetail obtenerDetalleProblema(Throwable throwable) {

        if(throwable instanceof BaseException baseException) {
            return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(baseException.getHttpStatusCode()),
                    baseException.getMessage());
        }

        if(throwable instanceof MethodNotAllowedException methodNotAllowedException) {
            final String FORMATO_MENSAJE = "El método HTTP %s no esta permitido para este endpoint";
            return ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED,
                    String.format(FORMATO_MENSAJE, methodNotAllowedException.getHttpMethod()));
        }

        if(throwable instanceof ServerWebInputException) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    "El formato del request enviado es inválido");
        }

        if(throwable instanceof UnsupportedMediaTypeStatusException) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "MediaType invalido");
        }

        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno");
    }
}