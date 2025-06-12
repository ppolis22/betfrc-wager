package com.buzz.betfrcwager.repo;

import com.buzz.betfrcwager.entity.Leg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LegRepository extends JpaRepository<Leg, String> {
    List<Leg> findAllByPropIdAndPropValue(String propId, String propValue);
}
