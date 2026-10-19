package br.com.francacodex.reservas.error;

import java.time.Instant;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public record Erro(String mensagem, String campo, Instant instante) {}
    private ResponseEntity<Erro> resposta(HttpStatus status, String mensagem, String campo) {
        return ResponseEntity.status(status).body(new Erro(mensagem, campo, Instant.now()));
    }
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Erro> naoEncontrado(RecursoNaoEncontradoException e) { return resposta(HttpStatus.NOT_FOUND, e.getMessage(), null); }
    @ExceptionHandler({ConflitoException.class, DataIntegrityViolationException.class})
    public ResponseEntity<Erro> conflito(Exception e) { return resposta(HttpStatus.CONFLICT, "Operacao viola uma regra de conflito ou unicidade", null); }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Erro> invalidacao(IllegalArgumentException e) { return resposta(HttpStatus.BAD_REQUEST, e.getMessage(), null); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Erro> validar(MethodArgumentNotValidException e) {
        var campo = e.getBindingResult().getFieldErrors().stream().findFirst();
        return resposta(HttpStatus.BAD_REQUEST, campo.map(x -> x.getDefaultMessage()).orElse("Dados invalidos"), campo.map(x -> x.getField()).orElse(null));
    }
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Erro> jsonInvalido(Exception e) { return resposta(HttpStatus.BAD_REQUEST, "JSON ou formato de data invalido", null); }
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Erro> parametroInvalido(Exception e) { return resposta(HttpStatus.BAD_REQUEST, "Parametro de rota ou consulta invalido", null); }
}
