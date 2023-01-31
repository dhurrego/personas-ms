package co.com.sofka.api.handler;

import co.com.sofka.model.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {

	private static final HttpStatus METHOD_NOT_ALLOWED = HttpStatus.METHOD_NOT_ALLOWED;
	private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
	private static final HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
	private static final HttpStatus UNSUPPORTED_MEDIA_TYPE = HttpStatus.UNSUPPORTED_MEDIA_TYPE;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ProblemDetail> manejarTodasExcepciones(Exception ex){
		registrarLog(ex);
		return ResponseEntity.internalServerError()
				.body(ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, "Error interno"));
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ProblemDetail> manejarBaseException(BaseException ex) {
		registrarLog(ex);
		return ResponseEntity.status(ex.getHttpStatusCode())
				.body(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(ex.getHttpStatusCode()), ex.getMessage()));
	}

	@ExceptionHandler(MethodNotAllowedException.class)
	public ResponseEntity<ProblemDetail> manejarMethodNotAllowedException(MethodNotAllowedException ex){
		registrarLog(ex);
		final String FORMATO_MENSAJE = "El método HTTP %s no esta permitido para este endpoint";
		return ResponseEntity.status(METHOD_NOT_ALLOWED)
				.body(ProblemDetail.forStatusAndDetail(METHOD_NOT_ALLOWED,
						String.format(FORMATO_MENSAJE, ex.getHttpMethod())));
	}

	@ExceptionHandler(ServerWebInputException.class)
	public ResponseEntity<ProblemDetail> manejarServerWebInputException(ServerWebInputException ex){
		registrarLog(ex);
		return ResponseEntity.badRequest()
				.body(ProblemDetail.forStatusAndDetail(BAD_REQUEST, "El formato del request enviado es inválido"));
	}

	@ExceptionHandler(UnsupportedMediaTypeStatusException.class)
	public ResponseEntity<ProblemDetail> manejarUnsupportedMediaTypeStatusException(UnsupportedMediaTypeStatusException ex){
		registrarLog(ex);
		return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE)
				.body(ProblemDetail.forStatusAndDetail(UNSUPPORTED_MEDIA_TYPE, "MediaType invalido"));
	}

	private void registrarLog(Throwable excepcion) {
		log.error(excepcion.toString());
	}
}