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
@Table(name="arching", uniqueConstraints = {@UniqueConstraint(name = "const_arc_date", columnNames = {"arcDate"})})
public class Cortes {
    @Id
    @Column(nullable = false)
    LocalDate arcDate;
    @Column(nullable = false)
    Boolean arcClosed;
    @Column(nullable = false)
    BigDecimal arcCash;
    @Column(nullable = false)
    BigDecimal arcDigital;
    @Column(nullable = false)
    BigDecimal arcApertureCash;
}
