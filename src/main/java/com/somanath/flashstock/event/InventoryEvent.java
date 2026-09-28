package com.somanath.flashstock.event;
public record InventoryEvent(String type,String reservationKey,String sku,int quantity) {}
