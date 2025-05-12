package com.appointmentService.controllers;


import com.appointmentService.domain.entity.Appointment;
import com.appointmentService.domain.entity.Procedure;
import com.appointmentService.repository.AppointmentRepository;
import com.appointmentService.dto.*;
import com.appointmentService.repository.ProcedureRepository;
import com.appointmentService.services.AppointmentModule;
import com.appointmentService.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    private final AppointmentRepository appointmentRepository;
    private final String jwtSecret = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";
    private final AppointmentModule appointmentModule;

    private final ProcedureRepository procedureRepository;
    public CalendarController(AppointmentRepository appointmentRepository, AppointmentModule appointmentModule, ProcedureRepository procedureRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentModule = appointmentModule;
        this.procedureRepository = procedureRepository;
    }

    @PostMapping("/doctor-appointments")
    public ResponseEntity<?> getDoctorAppointments(@RequestBody GetCalendarRequest request,
                                                                           @RequestHeader("Authorization") String token){
        try{
            if(token == null || token.isBlank()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Невалидный токен");
            }
            if(JwtUtils.isTokenExpired(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Токен истек");
            }

            Long doctorId = JwtUtils.getUserId(token);
            LocalDateTime start = request.getStartDate().atStartOfDay();
            LocalDateTime end = request.getEndDate().atTime(LocalTime.MAX);

            List<Appointment> appointments = appointmentRepository.findByDoctorIdAndStartTimeBetween(doctorId, start, end);

            List<GetCalendarResponse> response = appointments.stream()
                    .map(a -> {
                        GetCalendarResponse res = new GetCalendarResponse();
                        res.setId(a.getId());
                        res.setProcedureName(a.getProcedure().getName());
                        res.setStatusId(a.getStatusId());
                        res.setStartTime(a.getStartTime());
                        res.setEndTime(a.getEndTime());
                        return res;
                    }).collect(Collectors.toList());
            System.out.println("Привет мир");
            return ResponseEntity.ok(response);
        }
        catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(("Невалидный JWT токен: " + e.getMessage()));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении метода: "+e.getMessage());
        }
    }

    @GetMapping("/doctor-appointments/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id,
                                                                  @RequestHeader("Authorization") String token){
        try{
            AppointmentResponse dto = appointmentModule.getAppointmentById(id);
            if(dto == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Запись не найдена"));
            }
            return ResponseEntity.ok(dto);
        }
        catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(("Невалидный JWT токен: " + e.getMessage()));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении метода: "+e.getMessage());
        }
    }

    @PostMapping("/create-appointment")
    public ResponseEntity<?> createAppointment(@RequestHeader("Authorization") String token,
                                               @RequestBody CreateAppointmentRequest request){
        try{
            Appointment appointment = appointmentModule.createAppointment(request);
            if(appointment == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Процедура не найдена");
            }
            else {
                return ResponseEntity.ok(appointment);
            }

        }
        catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(("Невалидный JWT токен: " + e.getMessage()));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении метода: "+e.getMessage());
        }
    }

    @DeleteMapping("/doctor-appointments/{id}")
    public ResponseEntity<?> deleteAppointments(@RequestHeader("Authorization") String token,
                                                @PathVariable Long id){
        try{
            Optional<Appointment> optional = appointmentRepository.findById(id);
            if (optional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Запись не найдена");
            }
            appointmentRepository.deleteById(id);
            return ResponseEntity.ok("Запись успешно удалена");
        }
        catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(("Невалидный JWT токен: " + e.getMessage()));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении метода: "+e.getMessage());
        }
    }

    @PatchMapping("/doctor-appointments/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id,
                                               @RequestBody AppointmentUpdateRequest request,
                                               @RequestHeader("Authorization") String token){
        try{
            Optional<Appointment> optional = appointmentRepository.findById(id);
            if(optional.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Запись не найдена");
            }
            Appointment appointment = optional.get();

            Procedure procedure = procedureRepository.findById(request.getProcedureId()).orElse(null);
            if(procedure == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Процедура не найдена");
            }

            if (request.getPatientName() != null) {
                appointment.setPatientName(request.getPatientName());
            }
            if (request.getPatientContact() != null) {
                appointment.setPatientContact(request.getPatientContact());
            }
            if (request.getDoctorId() != null) {
                appointment.setDoctorId(request.getDoctorId());
            }
            if (request.getClinicId() != null) {
                appointment.setClinicId(request.getClinicId());
            }
            if (request.getStartTime() != null) {
                appointment.setStartTime(request.getStartTime());
            }
            if (request.getEndTime() != null) {
                appointment.setEndTime(request.getEndTime());
            }
            if (request.getStatusId() != null) {
                appointment.setStatusId(request.getStatusId());
            }
            if (request.getNotes() != null) {
                appointment.setNotes(request.getNotes());
            }
            if(request.getExternalPatientId() != null){
                appointment.setExternalPatientId(request.getExternalPatientId());
            }

            appointment.setProcedure(procedure);
            appointmentRepository.save(appointment);

            return ResponseEntity.ok("Запись успешно обновлена");

        }
        catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(("Невалидный JWT токен: " + e.getMessage()));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении метода: "+e.getMessage());
        }
    }


}
