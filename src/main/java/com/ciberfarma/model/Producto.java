package com.ciberfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @Column(name = "idprod")
    private String idProducto;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "precio")
    private BigDecimal precio;

    @ManyToOne
    @JoinColumn(name = "idcategoria")
    private Categoria idCategoria;

    @ManyToOne
    @JoinColumn(name = "estado")
    private Estado estado;
}
