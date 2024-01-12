package com.pdv.Test.Models.DTOs.Errors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    String err;
    String Message;
}
