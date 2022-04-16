package dev.dowell.springkafka;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {
    Optional<Order> findByCustomerId(String customerId);
}
