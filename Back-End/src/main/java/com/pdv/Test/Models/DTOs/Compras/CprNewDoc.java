package com.pdv.Test.Models.DTOs.Compras;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CprNewDoc {
    Boolean failed;
    Boolean openDoc;
    String clCode;
    LocalDate date;
    Integer invoice;
    String exceptionMessage;
    String clName;
}
