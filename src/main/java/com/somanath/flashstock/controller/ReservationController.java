package com.somanath.flashstock.controller;
import com.somanath.flashstock.dto.*; import com.somanath.flashstock.service.ReservationService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/reservations")
public class ReservationController{
 private final ReservationService service; public ReservationController(ReservationService s){service=s;}
 @PostMapping public ResponseEntity<?> reserve(@Valid @RequestBody ReservationRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.reserve(r));}
 @PostMapping("/{key}/confirm") public Object confirm(@PathVariable String key){return service.confirm(key);}
}
