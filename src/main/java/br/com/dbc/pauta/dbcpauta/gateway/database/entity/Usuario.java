package br.com.dbc.pauta.dbcpauta.gateway.database.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nome")
    private String nome;

    @ManyToMany(mappedBy = "usuariosQueVotaram")
    private List<SessaoVotacao> sessoesVotadas = new ArrayList<>();
}
