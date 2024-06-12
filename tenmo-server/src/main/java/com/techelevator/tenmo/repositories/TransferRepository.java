package com.techelevator.tenmo.repositories;
import com.techelevator.tenmo.entities.Transfer;
import com.techelevator.tenmo.entities.TransferUserNameProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByTransferId(Long id);
    @Query("SELECT tu.username AS userName FROM transfer t " +
            "JOIN account a ON t.account_from = a.account_id " +
            "JOIN tenmo_user tu ON a.user_id = tu.user_id " +
            "WHERE t.transfer_id = :transferId")
    TransferUserNameProjection findUserNameByTransferId(@Param("transferId") Integer transferId);
}
