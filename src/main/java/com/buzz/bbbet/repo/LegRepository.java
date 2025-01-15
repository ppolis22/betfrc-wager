package com.buzz.bbbet.repo;

import com.buzz.bbbet.entity.Leg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegRepository extends JpaRepository<Leg, String> { }
