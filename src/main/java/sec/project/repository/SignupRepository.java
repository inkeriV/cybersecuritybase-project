package sec.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Guest;

public interface SignupRepository extends JpaRepository<Guest, Long> {

}
