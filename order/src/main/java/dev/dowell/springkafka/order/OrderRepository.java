package dev.dowell.springkafka.order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {
    Optional<Order> findByCustomerId(String customerId);
    void deleteByCustomerId(String customerId);
}
