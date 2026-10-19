package br.com.francacodex.reservas.dto;

import br.com.francacodex.reservas.domain.*;
import java.time.LocalDateTime;

public final class Responses {
    private Responses() {}
    public record SalaResponse(Long id, String nome, int capacidade, boolean ativa) {
        public static SalaResponse of(Sala s) { return new SalaResponse(s.getId(), s.getNome(), s.getCapacidade(), s.isAtiva()); }
    }
    public record UsuarioResponse(Long id, String nome, String email) {
        public static UsuarioResponse of(Usuario u) { return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail()); }
    }
    public record ReservaResponse(Long id, Long salaId, Long usuarioId, LocalDateTime inicio, LocalDateTime fim, StatusReserva status) {
        public static ReservaResponse of(Reserva r) { return new ReservaResponse(r.getId(), r.getSala().getId(), r.getUsuario().getId(), r.getInicio(), r.getFim(), r.getStatus()); }
    }
}
