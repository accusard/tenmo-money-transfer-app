package com.techelevator.tenmo.repositories;
import com.techelevator.tenmo.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByTransferId(Long id);
    int findByAccountTo(long id);
    int findByAccountFrom(long id);
}
