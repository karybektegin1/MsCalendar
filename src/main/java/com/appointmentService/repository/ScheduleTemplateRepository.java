package com.appointmentService.repository;

import com.appointmentService.domain.entity.ScheduleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleTemplateRepository extends JpaRepository<ScheduleTemplate, Long> {

    List<ScheduleTemplate> findByDoctorIdAndDayOfWeek(Long doctorId, Integer dayOfWeek);

    List<ScheduleTemplate> findByDoctorId(Long doctorId);
}