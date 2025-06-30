package com.ciberfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_estados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estado {
    @Id
    @Column(name = "idestado")
    private Integer idEstado;

    @Column(name = "descripcion")
    private String descripcion;
}