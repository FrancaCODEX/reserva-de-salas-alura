package br.com.francacodex.reservas.repository;
import br.com.francacodex.reservas.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmailIgnoreCase(String email);
}
