package rim.bank_management_system.repository;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import rim.bank_management_system.entity.RefreshToken;
import rim.bank_management_system.entity.User;

import java.util.Date;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

}

