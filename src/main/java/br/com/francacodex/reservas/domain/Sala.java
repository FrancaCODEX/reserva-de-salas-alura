package br.com.francacodex.reservas.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "salas", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
public class Sala {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador estavel da sala
    @Column(nullable = false, unique = true)
    private String nome; // Nome unico para localizar a sala
    @Column(nullable = false)
    private int capacidade; // Numero maximo de participantes
    @Column(nullable = false)
    private boolean ativa = true; // Salas desativadas nao aceitam novas reservas

    protected Sala() {}
    public Sala(String nome, int capacidade, boolean ativa) { atualizar(nome, capacidade, ativa); }
    public void atualizar(String nome, int capacidade, boolean ativa) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("O nome da sala e obrigatorio");
        if (capacidade <= 0) throw new IllegalArgumentException("A capacidade deve ser positiva");
        this.nome = nome.trim(); this.capacidade = capacidade; this.ativa = ativa;
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public int getCapacidade() { return capacidade; }
    public boolean isAtiva() { return ativa; }
}
