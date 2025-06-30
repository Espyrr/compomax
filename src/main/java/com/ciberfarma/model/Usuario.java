package com.ciberfarma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private String correo;

    @NotBlank(message = "La clave es obligatoria")
    @Size(max = 5, message = "La clave no puede tener más de 5 caracteres")
    @Column(name = "clave")
    private String clave;

    @Column(name = "fnacim")
    private LocalDate fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "tipo")
    private Tipo idTipo;
}
