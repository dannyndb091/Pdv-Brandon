package com.pdv.Test.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="payments", uniqueConstraints = {@UniqueConstraint(name = "const_pay_doc", columnNames = {"payDoc"})})
public class Pagos {
    @Id
    @GeneratedValue
    Integer payId;
    @Column(nullable = false)
    Integer payDoc;
    @Column(nullable = false)
    Boolean payStatus;
    @Column(nullable = false)
    BigDecimal payCash;
    @Column(nullable = false)
    BigDecimal payDigital;
}
