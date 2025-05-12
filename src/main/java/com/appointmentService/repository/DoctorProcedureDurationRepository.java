package com.appointmentService.repository;

import com.appointmentService.domain.entity.DoctorProcedureDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorProcedureDurationRepository extends JpaRepository<DoctorProcedureDuration, Long> {

    Optional<DoctorProcedureDuration> findByDoctorIdAndProcedureId(Long doctorId, Long procedureId);

    List<DoctorProcedureDuration> findByDoctorId(Long doctorId);
}