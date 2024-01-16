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
@Table(name="inventory", uniqueConstraints = {@UniqueConstraint(name = "const_inv_pro", columnNames = {"invPro"})})
public class Inventario {
    @Id
    @GeneratedValue
    Integer invId;
    Integer invPro;
    Integer invBuys;
    BigDecimal invBuysValue;
    Integer invSells;
    BigDecimal invSellsValue;
    BigDecimal invDcrValue; //Valor de Producto Mermado.
    Integer invQty; //Disponible
    Integer invRes; //Reservado, venta no completado
    Integer invInc; //Producto por ingresar, compra no completada.
    Integer invDcr; //Producto Mermado.
}
