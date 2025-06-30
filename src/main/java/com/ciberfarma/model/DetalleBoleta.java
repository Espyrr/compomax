package com.ciberfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_det_boleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "idboleta")
    private Boleta boleta;

    @ManyToOne
    @JoinColumn(name = "idprod")
    private Producto producto;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;

    @Column(name = "importe")
    private BigDecimal importe;
}
