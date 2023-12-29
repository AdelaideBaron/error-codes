package com.wes.error_codes;

import lombok.Getter;

@Getter
public enum ErrorCode {
       ERROR_ABC("descriptionA"),
       ERROR_XYZ("descriptionX"),
       ERROR_DEF("descriptionD");
       // Add more error codes as needed

       public String description;

       ErrorCode(String description) {
              this.description = description;
       }
}
