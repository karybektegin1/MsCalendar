package com.appointmentService.controllers;


import com.appointmentService.domain.entity.Appointment;
import com.appointmentService.domain.entity.Procedure;
import com.appointmentService.dto.CreateProcedureRequest;
import com.appointmentService.dto.GetProcedureResponse;
import com.appointmentService.dto.ProcedureUpdateRequest;
import com.appointmentService.repository.ProcedureRepository;
import com.appointmentService.services.ProcedureModule;
import com.appointmentService.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/procedure")
public class ProcedureController {

    private final ProcedureRepository procedureRepository;

    private ProcedureModule procedureModule;

    public ProcedureController(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProcedure(@RequestHeader("Authorization") String token,
                                             @RequestBody CreateProcedureRequest request){
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }


        if (JwtUtils.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token expired");
        }

        try {
            // Create new procedure
            Procedure procedure = Procedure.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .defaultDurationMinutes(request.getDefaultDurationMinutes())
                    .build();

            // Save procedure
            procedureRepository.save(procedure);

            return ResponseEntity.ok("Procedure created successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating procedure: " + e.getMessage());
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> createProcedure(@RequestHeader("Authorization") String token,
                                             @RequestBody ProcedureUpdateRequest request,
                                             @PathVariable Long id){
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }
        if (JwtUtils.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token expired");
        }
        try {

            Optional<Procedure> optional = procedureRepository.findById(id);

            if(optional.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Процедура не найдена");
            }

            Procedure procedure = optional.get();

            if(!request.getName().isEmpty()){
                procedure.setName(request.getName());
            }
            if(request.getDescription() != null){
                procedure.setDescription(request.getDescription());
            }
            if(request.getDefaultDurationMinutes() != null){
                procedure.setDefaultDurationMinutes(request.getDefaultDurationMinutes());
            }
            procedureRepository.save(procedure);
            return ResponseEntity.ok("Сущность успешно обновлена");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating procedure: " + e.getMessage());
        }

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProcedure(@RequestHeader("Authorization") String token,
                                             @PathVariable Long id){
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }
        if (JwtUtils.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token expired");
        }
        try{
            Optional<Procedure> optional = procedureRepository.findById(id);

            if(optional.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Процедура не найдена");
            }
            Procedure procedure = optional.get();
            procedureRepository.deleteById(id);
            return ResponseEntity.ok("Сущность успешно удалена");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating procedure: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getProcedure(@RequestHeader("Authorization") String token){
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");
        }
        if (JwtUtils.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token expired");
        }
        try{

            List<Procedure> procedures = procedureRepository.findByOrderByNameAsc();
            return ResponseEntity.ok().body(procedures);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating procedure: " + e.getMessage());
        }
    }


}
