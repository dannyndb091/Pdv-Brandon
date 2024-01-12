package com.pdv.Test.Models.DTOs.Clientes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClient {
    String oldCode;
    String newCode;
    String newName;
    String newMail;
    String newCellphone;
    Integer newTypeClient;
    Boolean newStatus;
}
