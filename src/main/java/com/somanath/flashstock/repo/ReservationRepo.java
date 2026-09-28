package com.somanath.flashstock.repo;
import com.somanath.flashstock.model.Reservation;
import org.springframework.data.jpa.repository.*;
import java.time.Instant; import java.util.*;
public interface ReservationRepo extends JpaRepository<Reservation,Long>{
 Optional<Reservation> findByReservationKey(String key);
 List<Reservation> findTop100ByStatusAndExpiresAtBefore(Reservation.Status status, Instant now);
}
