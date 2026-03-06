package org.example.onlinevotingsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.onlinevotingsystem.models.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {
}
