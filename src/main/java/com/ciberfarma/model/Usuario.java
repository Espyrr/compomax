package com.ciberfarma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "tb_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer idUsuario;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "clave")
    private String clave;

    @Column(name = "fnacim")
    private LocalDate fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "tipo")
    private Tipo tipo;
}