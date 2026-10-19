package br.com.francacodex.reservas.repository;
import br.com.francacodex.reservas.domain.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SalaRepository extends JpaRepository<Sala, Long> {
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select s from Sala s where s.id = :id")
    java.util.Optional<Sala> findByIdForUpdate(@org.springframework.data.repository.query.Param("id") Long id);
    boolean existsByNomeIgnoreCase(String nome);
}
