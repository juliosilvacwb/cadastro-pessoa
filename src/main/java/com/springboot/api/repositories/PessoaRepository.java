package com.springboot.api.repositories;

import java.util.List;
import java.util.Optional;

import com.springboot.api.entities.Pessoa;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserRepository
 */


@RepositoryRestResource(collectionResourceRel = "pessoas", path = "pessoas")
@Transactional(readOnly=true)
public interface PessoaRepository extends PagingAndSortingRepository<Pessoa, Long>{

    @Query("select distinct(p) from Pessoa p where lower(p.nome) like lower(concat('%', :nome,'%'))")
    List<Pessoa> findByNome(@Param("nome") String nome, Pageable pageable);

	Optional<Pessoa> findByEmail(String string);

}
