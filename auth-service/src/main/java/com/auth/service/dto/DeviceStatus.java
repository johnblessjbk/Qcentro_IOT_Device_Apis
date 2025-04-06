package com.auth.service.dto;
public enum DeviceStatus {
 ONLINE, 
 OFFLINE, 
 ERROR;

 public static boolean isValid(String status) {
     try {
         DeviceStatus.valueOf(status);
         return true;
     } catch (IllegalArgumentException e) {
         return false;
     }
 }
}