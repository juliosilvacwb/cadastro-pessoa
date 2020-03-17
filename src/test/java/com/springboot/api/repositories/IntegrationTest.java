
package com.springboot.api.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.api.entities.Pessoa;
import com.springboot.api.enums.SexoEnum;
import com.springboot.api.utils.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * IntegrationTest
 */

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PessoaRepository pessoaRepository;

    @BeforeEach
    public void setUp() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(Utils.provideAuthentication());
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    void salvarPessoaTest() throws Exception {
        
        Pessoa pessoa = Pessoa.builder()
                                .nome("Fulano de Tal")
                                .email("fulano@email.com")
                                .sexo(SexoEnum.M)
                                .cpf("70293029075")
                                .naturalidade("naturalidade")
                                .nacionalidade("nacionalidade")
                                .dataNascimento(LocalDate.of(2000, 11, 15))
                                .build();
        
        mockMvc.perform(MockMvcRequestBuilders.post("/pessoas", 42L)
            .contentType("application/json")
            .param("sendWelcomeMail", "true")
            .content(objectMapper.writeValueAsString(pessoa)))
            .andExpect(MockMvcResultMatchers.status().isCreated());

        Optional<Pessoa> pessoaOptional = pessoaRepository.findByEmail("fulano@email.com");
        assertEquals(pessoaOptional.get().getEmail(), "fulano@email.com");
    }

    @Test
    void consultarPessoas() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pessoas")).andExpect(MockMvcResultMatchers.status().isOk());
    }

}