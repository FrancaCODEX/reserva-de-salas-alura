package br.com.francacodex.reservas.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public final class Requests {
    private Requests() {}
    public record SalaRequest(@NotBlank String nome, @NotNull @Positive Integer capacidade, @NotNull Boolean ativa) {}
    public record UsuarioRequest(@NotBlank String nome, @NotBlank @Email String email) {}
    public record ReservaRequest(@NotNull Long salaId, @NotNull Long usuarioId,
        @NotNull LocalDateTime inicio, @NotNull LocalDateTime fim) {}
}
