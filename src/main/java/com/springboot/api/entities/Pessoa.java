package com.springboot.api.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.api.enums.SexoEnum;

import org.hibernate.validator.constraints.br.CPF;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Pessoas
 */
@Entity
@Table(name = "pessoa")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotEmpty(message = "Nome não pode ser vazio.")
    private String nome;

    private SexoEnum sexo;
    
    @Column(unique=true, nullable = true)
    @Email(message = "Email inválido.")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    
    private String naturalidade;
    private String nacionalidade;

    @Column(unique=true)
    @CPF(message = "CPF inválido.")
    private String cpf;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime updateDate;

    @Version
    private Integer version;

    @JsonIgnore
    public void createDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    
    @JsonIgnore
    public void updateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime date = LocalDateTime.now();
        this.createDate = date;
        this.updateDate = date;
    }
    
}