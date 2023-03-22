package dev.dowell.springkafka.restaurant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantOrderRepository extends CrudRepository<RestaurantOrder, String> {
}
