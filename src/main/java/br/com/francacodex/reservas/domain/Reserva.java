package br.com.francacodex.reservas.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas", indexes = @Index(name = "idx_reservas_sala_periodo", columnList = "sala_id,inicio,fim"))
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador da reserva
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala; // Recurso reservado
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Responsavel pela reserva
    @Column(nullable = false)
    private LocalDateTime inicio; // Inicio inclusivo do intervalo
    @Column(nullable = false)
    private LocalDateTime fim; // Fim exclusivo do intervalo
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private StatusReserva status = StatusReserva.ATIVA; // Canceladas nao bloqueiam horarios

    protected Reserva() {}
    public Reserva(Sala sala, Usuario usuario, LocalDateTime inicio, LocalDateTime fim) {
        if (sala == null || usuario == null) throw new IllegalArgumentException("Sala e usuario sao obrigatorios");
        if (!sala.isAtiva()) throw new IllegalArgumentException("Nao e possivel reservar uma sala inativa");
        this.sala = sala; this.usuario = usuario; alterarPeriodo(inicio, fim);
    }
    public void alterarPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null || !inicio.isBefore(fim)) throw new IllegalArgumentException("O inicio deve ser anterior ao fim");
        if (status == StatusReserva.CANCELADA) throw new IllegalArgumentException("Reservas canceladas nao podem ser alteradas");
        this.inicio = inicio; this.fim = fim;
    }
    public boolean sobrepoe(LocalDateTime outroInicio, LocalDateTime outroFim) {
        return status == StatusReserva.ATIVA && inicio.isBefore(outroFim) && outroInicio.isBefore(fim);
    }
    public void alterarParticipantes(Sala sala, Usuario usuario) {
        if (status == StatusReserva.CANCELADA) throw new IllegalArgumentException("Reservas canceladas nao podem ser alteradas");
        if (sala == null || usuario == null || !sala.isAtiva()) throw new IllegalArgumentException("Sala e usuario validos sao obrigatorios");
        this.sala = sala; this.usuario = usuario;
    }
    public void cancelar() { status = StatusReserva.CANCELADA; }
    public Long getId() { return id; }
    public Sala getSala() { return sala; }
    public Usuario getUsuario() { return usuario; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFim() { return fim; }
    public StatusReserva getStatus() { return status; }
}
