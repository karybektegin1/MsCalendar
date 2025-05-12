package com.appointmentService.repository;

import com.appointmentService.domain.entity.ScheduleException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleExceptionRepository extends JpaRepository<ScheduleException, Long> {

    List<ScheduleException> findByDoctorIdAndExceptionDateBetween(
            Long doctorId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT se FROM ScheduleException se WHERE se.doctorId = :doctorId " +
            "AND se.exceptionDate = :date")
    List<ScheduleException> findByDoctorIdAndDate(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date);
}