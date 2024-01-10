package com.pdv.Test.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="documents", uniqueConstraints = {@UniqueConstraint(name = "const_doc_Invoice", columnNames = {"docInvoice"})})
public class Documentos {
    @Id
    @GeneratedValue
    Integer docId;
    LocalDate docDate;
    Integer docIdCliente;
    Integer docType; //1 = Compra, 2 = Venta
    Boolean docCompleted;
    Boolean docStatus;
    BigDecimal docNet;
    BigDecimal docDiscount;
    BigDecimal docSubtotal;
    BigDecimal docTax;
    BigDecimal docTotal;
    Integer docProductQty;
}
