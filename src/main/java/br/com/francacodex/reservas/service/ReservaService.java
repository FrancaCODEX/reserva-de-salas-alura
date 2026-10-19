package br.com.francacodex.reservas.service;

import br.com.francacodex.reservas.domain.*;
import br.com.francacodex.reservas.dto.Requests.ReservaRequest;
import br.com.francacodex.reservas.error.*;
import br.com.francacodex.reservas.repository.*;
import java.time.LocalDateTime;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {
    private final ReservaRepository reservas;
    private final SalaRepository salas;
    private final UsuarioRepository usuarios;
    public ReservaService(ReservaRepository reservas, SalaRepository salas, UsuarioRepository usuarios) {
        this.reservas = reservas; this.salas = salas; this.usuarios = usuarios;
    }
    @Transactional(readOnly = true)
    public Page<Reserva> listar(Long salaId, LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        if (inicio != null && fim != null && !inicio.isBefore(fim)) throw new IllegalArgumentException("Periodo de consulta invalido");
        return reservas.consultar(salaId, inicio, fim, pageable);
    }
    @Transactional(readOnly = true) public Reserva buscar(Long id) {
        return reservas.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Reserva nao encontrada: " + id));
    }
    @Transactional
    public Reserva criar(ReservaRequest r) {
        var sala = salas.findByIdForUpdate(r.salaId()).orElseThrow(() -> new RecursoNaoEncontradoException("Sala nao encontrada: " + r.salaId()));
        var usuario = usuarios.findById(r.usuarioId()).orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado: " + r.usuarioId()));
        var reserva = new Reserva(sala, usuario, r.inicio(), r.fim());
        verificarConflito(sala.getId(), reserva.getInicio(), reserva.getFim(), -1L);
        return reservas.save(reserva);
    }
    @Transactional
    public Reserva atualizar(Long id, ReservaRequest r) {
        var atual = buscar(id);
        if (atual.getStatus() == StatusReserva.CANCELADA) throw new ConflitoException("Reserva cancelada nao pode ser atualizada");
        // Lock da sala alvo serializa verificacao de conflitos entre operacoes concorrentes.
        var sala = salas.findByIdForUpdate(r.salaId()).orElseThrow(() -> new RecursoNaoEncontradoException("Sala nao encontrada: " + r.salaId()));
        var usuario = usuarios.findById(r.usuarioId()).orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado: " + r.usuarioId()));
        if (!sala.isAtiva()) throw new IllegalArgumentException("Nao e possivel reservar uma sala inativa");
        verificarPeriodo(r.inicio(), r.fim());
        verificarConflito(sala.getId(), r.inicio(), r.fim(), id);
        atual.alterarParticipantes(sala, usuario);
        atual.alterarPeriodo(r.inicio(), r.fim());
        return atual;
    }
    @Transactional public void cancelar(Long id) { buscar(id).cancelar(); }
    private void verificarPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null || !inicio.isBefore(fim)) throw new IllegalArgumentException("O inicio deve ser anterior ao fim");
    }
    private void verificarConflito(Long salaId, LocalDateTime inicio, LocalDateTime fim, Long ignorarId) {
        if (reservas.existeConflito(salaId, StatusReserva.ATIVA, inicio, fim, ignorarId)) {
            throw new ConflitoException("Ja existe uma reserva ativa para essa sala no periodo informado");
        }
    }
}
