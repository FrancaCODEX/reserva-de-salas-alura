package br.com.francacodex.reservas.domain;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ReservaTest {
    private final Sala sala = new Sala("Azul", 8, true);
    private final Usuario usuario = new Usuario("Ana", "ana@exemplo.com");
    private LocalDateTime data(int hora, int minuto) { return LocalDateTime.of(2026, 10, 20, hora, minuto); }

    @Test void deveDetectarSobreposicaoReal() {
        var reserva = new Reserva(sala, usuario, data(9, 0), data(10, 0));
        assertTrue(reserva.sobrepoe(data(9, 30), data(10, 30)));
        assertTrue(reserva.sobrepoe(data(8, 30), data(9, 30)));
        assertTrue(reserva.sobrepoe(data(9, 0), data(10, 0)));
    }
    @Test void devePermitirIntervalosAdjacentes() {
        var reserva = new Reserva(sala, usuario, data(9, 0), data(10, 0));
        assertFalse(reserva.sobrepoe(data(10, 0), data(11, 0)));
        assertFalse(reserva.sobrepoe(data(8, 0), data(9, 0)));
    }
    @Test void reservaCanceladaNaoConflita() {
        var reserva = new Reserva(sala, usuario, data(9, 0), data(10, 0));
        reserva.cancelar();
        assertFalse(reserva.sobrepoe(data(9, 30), data(10, 30)));
        assertEquals(StatusReserva.CANCELADA, reserva.getStatus());
        assertThrows(IllegalArgumentException.class, () -> reserva.alterarPeriodo(data(11, 0), data(12, 0)));
    }
    @Test void deveRejeitarDatasInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> new Reserva(sala, usuario, data(10, 0), data(10, 0)));
        assertThrows(IllegalArgumentException.class, () -> new Reserva(sala, usuario, data(11, 0), data(10, 0)));
    }
    @Test void deveRejeitarSalaInativa() {
        var inativa = new Sala("Cinza", 5, false);
        assertThrows(IllegalArgumentException.class, () -> new Reserva(inativa, usuario, data(9, 0), data(10, 0)));
    }
    @Test void capacidadeDeveSerPositiva() {
        assertThrows(IllegalArgumentException.class, () -> new Sala("Erro", 0, true));
    }
}
