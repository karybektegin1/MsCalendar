package com.appointmentService.repository;

import com.appointmentService.domain.entity.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

    List<Procedure> findByOrderByNameAsc();
    Optional<Procedure> findById(Long id);

}