package com.somanath.flashstock.repo;
import com.somanath.flashstock.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface ProductRepo extends JpaRepository<Product,Long>{
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select p from Product p where p.sku=:sku") Optional<Product> findForUpdate(@Param("sku") String sku);
}
