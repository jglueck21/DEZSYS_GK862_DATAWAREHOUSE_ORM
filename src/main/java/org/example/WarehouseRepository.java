package org.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends CrudRepository<WarehouseData, Long> {
    Optional<WarehouseData> findByWarehouseID(String warehouseID);
}
