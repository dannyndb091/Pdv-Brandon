package com.pdv.Test.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="clienteproveedor", uniqueConstraints = {@UniqueConstraint(name = "const_cl_cod", columnNames = {"clCode"})})
public class ClienteProveedor {
    @Id
    @GeneratedValue
    Integer clId;
    @Column(nullable = false, length = 12)
    String clCode;
    @Column(nullable = false, length = 50)
    String clName;
    @Column(nullable = false)
    Integer clTypeClient; //0 = Cliente, 1 = Proveedor, 2 = Ambos
    @Column(nullable = false)
    Boolean clStatus;
}
