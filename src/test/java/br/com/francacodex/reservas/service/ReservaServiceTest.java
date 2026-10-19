package br.com.francacodex.reservas.service;

import br.com.francacodex.reservas.domain.*;
import br.com.francacodex.reservas.dto.Requests.ReservaRequest;
import br.com.francacodex.reservas.error.*;
import br.com.francacodex.reservas.repository.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class ReservaServiceTest {
    @Mock ReservaRepository reservas;
    @Mock SalaRepository salas;
    @Mock UsuarioRepository usuarios;
    @InjectMocks ReservaService service;
    Sala sala;
    Usuario usuario;
    ReservaRequest pedido;

    @BeforeEach void iniciar() {
        MockitoAnnotations.openMocks(this);
        sala = new Sala("Sala Azul", 10, true);
        usuario = new Usuario("Ana", "ana@exemplo.com");
        pedido = new ReservaRequest(1L, 2L, LocalDateTime.of(2026, 10, 20, 9, 0), LocalDateTime.of(2026, 10, 20, 10, 0));
        when(salas.findByIdForUpdate(1L)).thenReturn(Optional.of(sala));
        when(usuarios.findById(2L)).thenReturn(Optional.of(usuario));
    }
    @Test void criaReservaSemConflito() {
        when(reservas.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));
        var criada = service.criar(pedido);
        assertEquals(StatusReserva.ATIVA, criada.getStatus());
        verify(reservas).save(any(Reserva.class));
    }
    @Test void retornaConflitoParaSalaOcupada() {
        when(reservas.existeConflito(eq(1L), eq(StatusReserva.ATIVA), any(), any(), eq(-1L))).thenReturn(true);
        assertThrows(ConflitoException.class, () -> service.criar(pedido));
        verify(reservas, never()).save(any());
    }
    @Test void cancelaReservaSemExcluir() {
        var reserva = new Reserva(sala, usuario, pedido.inicio(), pedido.fim());
        when(reservas.findById(8L)).thenReturn(Optional.of(reserva));
        service.cancelar(8L);
        assertEquals(StatusReserva.CANCELADA, reserva.getStatus());
        verify(reservas, never()).delete(any());
    }
    @Test void deveRetornar404ParaReservaInexistente() {
        when(reservas.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscar(999L));
    }
}
