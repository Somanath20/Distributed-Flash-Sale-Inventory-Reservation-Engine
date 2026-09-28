package com.somanath.flashstock.service;

import com.somanath.flashstock.dto.ReservationRequest;
import com.somanath.flashstock.event.InventoryEvent;
import com.somanath.flashstock.model.*;
import com.somanath.flashstock.repo.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration; import java.time.Instant; import java.util.UUID;

@Service
public class ReservationService {
 private final ProductRepo products; private final ReservationRepo reservations; private final KafkaTemplate<String,InventoryEvent> kafka; private final StringRedisTemplate redis;
 public ReservationService(ProductRepo p,ReservationRepo r,KafkaTemplate<String,InventoryEvent> k,StringRedisTemplate redis){this.products=p;this.reservations=r;this.kafka=k;this.redis=redis;}
 @Transactional
 public Reservation reserve(ReservationRequest req){
   var existing=reservations.findByReservationKey(req.idempotencyKey()); if(existing.isPresent()) return existing.get();
   String lock="flashstock:lock:"+req.sku(); Boolean acquired=redis.opsForValue().setIfAbsent(lock,"1",Duration.ofSeconds(5));
   if(!Boolean.TRUE.equals(acquired)) throw new IllegalStateException("Product is busy; retry request");
   try {
     Product p=products.findForUpdate(req.sku()).orElseThrow(()->new IllegalArgumentException("SKU not found"));
     p.reserve(req.quantity()); products.save(p);
     Reservation r=reservations.save(new Reservation(req.idempotencyKey(),req.sku(),req.quantity(),Instant.now().plusSeconds(120)));
     kafka.send("inventory-events",req.sku(),new InventoryEvent("RESERVED",r.getReservationKey(),r.getSku(),r.getQuantity()));
     return r;
   } finally { redis.delete(lock); }
 }
 @Transactional public Reservation confirm(String key){
   Reservation r=reservations.findByReservationKey(key).orElseThrow(()->new IllegalArgumentException("Reservation not found"));
   if(r.getStatus()!=Reservation.Status.ACTIVE) return r;
   if(r.getExpiresAt().isBefore(Instant.now())) { expire(r); return r; }
   r.confirm(); kafka.send("inventory-events",r.getSku(),new InventoryEvent("CONFIRMED",r.getReservationKey(),r.getSku(),r.getQuantity())); return r;
 }
 @Scheduled(fixedDelay=10000) @Transactional public void expireReservations(){
   reservations.findTop100ByStatusAndExpiresAtBefore(Reservation.Status.ACTIVE,Instant.now()).forEach(this::expire);
 }
 private void expire(Reservation r){
   Product p=products.findForUpdate(r.getSku()).orElseThrow(); p.release(r.getQuantity()); r.expire();
   kafka.send("inventory-events",r.getSku(),new InventoryEvent("RELEASED",r.getReservationKey(),r.getSku(),r.getQuantity()));
 }
}
