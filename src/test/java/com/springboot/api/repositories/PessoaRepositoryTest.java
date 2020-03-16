package com.springboot.api.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.springboot.api.entities.Pessoa;
import com.springboot.api.enums.SexoEnum;
import com.springboot.api.utils.Utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * UserRepositoryTest
 */
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class PessoaRepositoryTest {

    @Autowired
    private PessoaRepository pessoaRepository;
    
    private Pessoa pessoa;
    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(Utils.provideAuthentication());
        SecurityContextHolder.setContext(securityContext);

        this.pageable = PageRequest.of(0, 25, Sort.by(Order.asc("nome")));

        pessoa = Pessoa.builder()
                        .nome("Fulano de Tal")
                        .email("fulano@email.com")
                        .sexo(SexoEnum.M)
                        .cpf("70293029075")
                        .naturalidade("naturalidade")
                        .nacionalidade("nacionalidade")
                        .dataNascimento(LocalDate.of(2000, 11, 15))
                        .build();

        this.pessoaRepository.save(pessoa);
    }

    @AfterEach
    public void tearDown() {
        this.pessoaRepository.deleteAll();
    }

    @Test
    public void testFindPessoaById() {
        Optional<Pessoa> pessoaOp = this.pessoaRepository.findById(this.pessoa.getId());
        assertNotNull(pessoaOp.get());
    }
    
    @Test
    public void testFindPessoaByNome() {
        List<Pessoa> pessoas = this.pessoaRepository.findByNome("Fulano", this.pageable);
        assertEquals(1, pessoas.size());
    }
   
    @Test
    public void testFindUserByEmail() {
        Optional<Pessoa> pessoa = this.pessoaRepository.findByEmail("fulano@email.com");
        assertNotNull(pessoa.get());
    }
   
}
