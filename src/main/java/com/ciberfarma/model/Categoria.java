package com.ciberfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @Column(name = "idcategoria")
    private Integer idCategoria;

    @Column(name = "descripcion")
    private String descripcion;
}