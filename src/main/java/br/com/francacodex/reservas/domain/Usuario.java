package br.com.francacodex.reservas.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador estavel de quem reserva
    @Column(nullable = false)
    private String nome; // Nome de exibicao
    @Column(nullable = false, unique = true)
    private String email; // Contato e chave natural unica

    protected Usuario() {}
    public Usuario(String nome, String email) { atualizar(nome, email); }
    public void atualizar(String nome, String email) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome do usuario e obrigatorio");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email e obrigatorio");
        this.nome = nome.trim(); this.email = email.trim().toLowerCase();
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
}
