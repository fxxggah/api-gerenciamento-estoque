package com.estoque.gerenciamentoestoque.service;

import com.estoque.gerenciamentoestoque.dto.category.CategoryRequestDTO;
import com.estoque.gerenciamentoestoque.dto.category.CategoryResponseDTO;
import com.estoque.gerenciamentoestoque.entity.Category;
import com.estoque.gerenciamentoestoque.exception.BusinessException;
import com.estoque.gerenciamentoestoque.exception.ResourceNotFoundException;
import com.estoque.gerenciamentoestoque.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;


    // CRUD Testes

    @Test
    @DisplayName("Deve criar uma categoria.")
    void deveCriarUmaCategoria(){

        // Cria a entidade categoria que sera salva pelo repository
        Category categoryEntity = new Category(1L, "Mouse", null);

        // Cria a categoria de request que sera passada como parametro no service
        CategoryRequestDTO categoryRequest = new CategoryRequestDTO("Mouse");

        // Configura o mock para retornar falso quando verificar se a categoria existe por nome
        when(categoryRepository.existsByName(categoryRequest.getName())).thenReturn(false);

        // Configura o mock para retornar a entidade da categoria salva
        when(categoryRepository.save(any(Category.class))).thenReturn(categoryEntity);

        // Chama o método a ser testado
        CategoryResponseDTO result = categoryService.save(categoryRequest);

        // Verifica se o resultado é o esperado
        assertNotNull(result);
        assertEquals(categoryRequest.getName(), result.getName());

        // Verifica se os métodos do repository foram chamados
        verify(categoryRepository).save(any(Category.class));
        verify(categoryRepository).existsByName(categoryRequest.getName());
    }

    @Test
    @DisplayName("Deve listar as categorias convertidos em DTOs.")
    void deveListarCategoriasConvertidosEmDTOs(){

        // Cria as categorias que serao listadas pelo repository
        Category categoryOne = new Category(1L, "Mouse", null);
        Category categoryTwo = new Category(2L, "Teclado", null);

        // Configura o mock para retornar as categorias
        when(categoryRepository.findAll()).thenReturn(List.of(categoryOne, categoryTwo));

        // Chama o método a ser testado
        List<CategoryResponseDTO> result = categoryService.findAll();

        // Verifica se o resultado é o esperado
        assertEquals(2, result.size());
        assertEquals("Mouse", result.get(0).getName());
        assertEquals("Teclado", result.get(1).getName());

        // Verifica se o método do repository foram chamados
        verify(categoryRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar a categoria por id convertido em DTO.")
    void deveRetornarCategoriasPorIdConvertidoEmDTO(){

        // Cria a categoria que sera buscada pelo repository
        Category category = new Category(1L, "Mouse", null);

        // Configura o mock para retornar a categoria pelo Id
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Chama o método a ser testado
        Optional<CategoryResponseDTO> result = Optional.ofNullable(categoryService.findById(1L));

        // Verifica se o resultado é o esperado
        assertNotNull(result);
        assertEquals(category.getId(), result.get().getId());
        assertEquals(category.getName(), result.get().getName());

        // Verifica se o método do repository foram chamados
        verify(categoryRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve deletar uma categoria por Id.")
    void deveDeletarCategoriaPorId(){

        Category category = new Category(1L, "Mouse", null);

        // Configura o mock para encontrar a categoria pelo Id
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Chama o método a ser testado
        categoryService.delete(1L);

        // Verifica se os métodos do repository foram chamados
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).delete(category);
    }


    // Exceptions Testes

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o ID da categoria não existir")
    void deveLancarExcecaoQuandoIdNaoExistir() {

        // Cria o Id inexistente
        Long idInexistente = 99L;

        // Configura o mock para retornar vazio
        when(categoryRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Verifica se a exceção correta foi lançada
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.delete(idInexistente);
        });

        // Verificar se a mensagem da exceção está correta
        assertEquals("Categoria não encontrada", exception.getMessage());

        // Verifica que o delete NUNCA foi chamado
        verify(categoryRepository, never()).delete(any(Category.class));
        verify(categoryRepository).findById(idInexistente);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando nome da categoria ja existir")
    void deveLancarExcecaoQuandoNomeDaCategoriaJaExistir() {

        // Cria a categoria que sera encotrada pelo repository
        CategoryRequestDTO category = new CategoryRequestDTO("Mouse");

        // Configura o mock para retornar verdadeiro
        when(categoryRepository.existsByName(category.getName())).thenReturn(true);

        // Verifica se a exceção correta foi lançada
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            categoryService.save(category);
        });

        // Verificar se a mensagem da exceção está correta
        assertEquals("Categoria já existe", exception.getMessage());

        // Verifica que o save NUNCA foi chamado
        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryRepository).existsByName(category.getName());
    }

}