package com.appointmentService.repository;

import com.appointmentService.domain.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorIdAndStartTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByExternalPatientId(Long patientId);

    Optional<Appointment> findById(Long id);

    List<Appointment> findByClinicIdAndStartTimeBetween(Long clinicId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND " +
            "((a.startTime <= :end AND a.endTime >= :start) AND a.statusId IN (1, 2))")
    List<Appointment> findOverlappingAppointments(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<Appointment> findByPatientContactAndStatusIdIn(String contact, List<Integer> statusIds);
}
