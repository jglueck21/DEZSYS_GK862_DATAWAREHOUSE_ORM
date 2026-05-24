package org.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<ProductData, Long> {
    Optional<ProductData> findByWarehouseData_WarehouseIDAndProductID(String warehouseID, String productID);
}
