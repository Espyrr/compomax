package com.ciberfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_boleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idboleta")
    private Integer idBoleta;

    @Column(name = "fch_boleta")
    private LocalDateTime fechaBoleta;

    @ManyToOne
    @JoinColumn(name = "idusuario")
    private Usuario usuario;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "igv")
    private BigDecimal igv;

    @Column(name = "total")
    private BigDecimal total;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleBoleta> detalles;
}
