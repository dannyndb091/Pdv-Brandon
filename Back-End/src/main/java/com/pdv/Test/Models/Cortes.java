package com.pdv.Test.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="arching", uniqueConstraints = {@UniqueConstraint(name = "const_arc_date", columnNames = {"arcDate"})})
public class Cortes {
    @Id
    @GeneratedValue
    Integer arcId;
    @Column(nullable = false)
    Date arcDate;
    @Column(nullable = false)
    Boolean arcClosed;
    @Column(nullable = false)
    BigDecimal arcCash;
    @Column(nullable = false)
    BigDecimal arcDigital;
}
