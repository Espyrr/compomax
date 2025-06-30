package com.ciberfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_tipos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tipo {
    @Id
    @Column(name = "idtipo")
    private Integer idTipo;

    @Column(name = "descripcion")
    private String descripcion;
}