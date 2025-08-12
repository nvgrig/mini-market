package ru.nvgrig.manager.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nvgrig.manager.entity.MarketUser;

import java.util.Optional;

public interface MarketUserRepository extends CrudRepository<MarketUser, Integer> {

    Optional<MarketUser> findByUsername(String username);
}
