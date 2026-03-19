package com.estoque.gerenciamentoestoque.service;

import com.estoque.gerenciamentoestoque.dto.product.ProductRequestDTO;
import com.estoque.gerenciamentoestoque.dto.product.ProductResponseDTO;
import com.estoque.gerenciamentoestoque.entity.Category;
import com.estoque.gerenciamentoestoque.entity.Product;
import com.estoque.gerenciamentoestoque.exception.ResourceNotFoundException;
import com.estoque.gerenciamentoestoque.repository.CategoryRepository;
import com.estoque.gerenciamentoestoque.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    // CRUD Testes

    @Test
    @DisplayName("Deve criar um produto.")
    void deveCriarProduto(){

        // Cria a categoria que sera usada do produto
        Category category = new Category(1L, "Mouse", null);

        // Cria o produto que sera salvo
        Product product = new Product(1L, "Attack Shark X11", category, "Mouse Gamer com Dock", new BigDecimal("179.90"), 3, LocalDateTime.now());

        ProductRequestDTO productRequest = new ProductRequestDTO("Attack Shark X11", 1L, "Mouse Gamer com Dock", new BigDecimal("179.90"), 3);

        // Configura o comportamento do mock para retornar o produto salvo
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Configura o comportamento do mock para retornar a categoria por Id
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Chama o método a ser testado
        ProductResponseDTO result = productService.save(productRequest);

        // Verifica se o resultado é o esperado
        assertEquals(1L, result.getId());
        assertEquals("Attack Shark X11", result.getName());
        assertEquals(new BigDecimal("179.90"), result.getPrice());
        assertEquals(3, result.getAmount());

        // Verifica se os métodos do repository foram chamados
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));

    }

    @Test
    @DisplayName("Deve listar todos os produtos convertidos para ProductResponseDTO.")
    void deveListarProdutosConvertidosEmDTOs() {

        // Cria a categoria que sera usada do produto
        Category category = new Category(1L, "Mouse", null);

        // Cria os produtos que seram retornados na lista de produtos
        Product productOne = new Product(1L, "Attack Shark X11", category, "Mouse Gamer com Dock", new BigDecimal("179.90"), 3, LocalDateTime.now());
        Product productTwo = new Product(2L, "Attack Shark R1", category, "Mouse Gamer sem Dock", new BigDecimal("149.90"), 3, LocalDateTime.now());

        // Cria a lista de produtos que sera retornada
        List<Product> products = List.of(productOne, productTwo);

        // Configura o comportamento do mock para retornar a lista de produtos
        when(productRepository.findAll()).thenReturn(products);

        // Chama o método a ser testado
        List<ProductResponseDTO> result = productService.findAll();

        // Verifica se o resultado é o esperado
        assertEquals(2 , result.size());
        assertEquals("Attack Shark X11", result.get(0).getName());
        assertEquals(new BigDecimal("179.90"), result.get(0).getPrice());
        assertEquals("Attack Shark R1", result.get(1).getName());
        assertEquals(new BigDecimal("149.90"), result.get(1).getPrice());

        // Verifica se o método do repository foi chamado
        verify(productRepository).findAll();

    }

    @Test
    @DisplayName("Deve retornar um produto por Id convertido para ProductResponseDTO.")
    void deveRetornarProdutoPorIdConvertidoEmDTO() {

        // Cria a categoria que sera usada do produto
        Category category = new Category(1L, "Mouse", null);

        // Cria o produto que sera retornado por Id
        Optional<Product> product = Optional.of(new Product(1L, "Attack Shark X11", category, "Mouse Gamer com Dock", new BigDecimal("179.90"), 3, LocalDateTime.now()));

        // Configura o comportamento do mock para retornar o produto por Id
        when(productRepository.findById(1L)).thenReturn(product);

        ProductResponseDTO result = productService.findById(1L);

        // Verifica se o resultado é o esperado
        assertEquals("Attack Shark X11", result.getName());
        assertEquals(new BigDecimal("179.90"), result.getPrice());

        // Verifica se o método do repository foi chamado
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar uma lista de produtos por categoria.")
    void deveRetornarProdutoPorCategoriaConvertidoEmDTO() {

        // Cria a categoria que sera usada no produto
        Category category = new Category(1L, "Mouse", null);

        // Cria os produtos que seram retornados por categoria
        Product productOne = new Product(1L, "Attack Shark X11", category, "Mouse Gamer com Dock", new BigDecimal("179.90"), 3, LocalDateTime.now());
        Product productTwo = new Product(2L, "Attack Shark R1", category, "Mouse Gamer sem Dock", new BigDecimal("149.90"), 3, LocalDateTime.now());

        // Cria a lista de produtos que sera retornada
        List<Product> products = List.of(productOne, productTwo);

        // Configura o comportamento do mock para retornar a lista de produtos por categoria
        when(productRepository.findByCategoryId(1L)).thenReturn(products);

        List<ProductResponseDTO> result = productService.findByCategory(1L);

        // Verifica se o resultado é o esperado
        assertEquals(2 , result.size());
        assertEquals("Attack Shark X11", result.get(0).getName());
        assertEquals(new BigDecimal("179.90"), result.get(0).getPrice());
        assertEquals("Attack Shark R1", result.get(1).getName());
        assertEquals(new BigDecimal("149.90"), result.get(1).getPrice());

        // Verifica se o método do repository foi chamado
        verify(productRepository).findByCategoryId(1L);

    }

    @Test
    @DisplayName("Deve atualizar um produto por Id.")
    void deveAtualizarProdutoPorId(){

        // Cria a categoria que sera usada do produto
        Category category = new Category(1L, "Mouse", null);

        // Cria o produto que sera atualizado por Id
        Product product = new Product(1L, "Attack Shark X111", category, "Mouse Gamer com Dock", new BigDecimal("149.90"), 0, LocalDateTime.now());

        // Cria o ProductRequestDTO com os dados atualizados do produto
        ProductRequestDTO newProduct = new ProductRequestDTO("Attack Shark X11", 1L, "Mouse Gamer com Dock", new BigDecimal("179.90"), 3);

        // Configura o comportamento do mock para retornar o produto por Id
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Configura o comportamento do mock para retornar a categoria por Id
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO result = productService.update(1L, newProduct);

        // Verifica se o resultado é o esperado
        assertEquals("Attack Shark X11", result.getName());
        assertEquals(new BigDecimal("179.90"), result.getPrice());
        assertEquals(3, result.getAmount());

        // Verifica se os métodos do repository foram chamados
        verify(productRepository).findById(1L);
        verify(categoryRepository).findById(1L);
        verify(productRepository).save(any(Product.class));

    }

    @Test
    @DisplayName("Deve deletar um produto por Id.")
    void deveDeletarProdutoPorId() {

        // Cria a categoria que sera usada do produto
        Category category = new Category(1L, "Mouse", null);

        // Cria o produto que sera deletado por Id
        Product product = new Product(1L, "Attack Shark X11", category, "Mouse Gamer com Dock", new BigDecimal("179.90"), 0, LocalDateTime.now());

        // Configura o comportamento do mock para retornar o produto por Id
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Chama o método a ser testado
        productService.delete(1L);

        // Verifica se os métodos do repository foram chamados
        verify(productRepository).findById(1L);
        verify(productRepository).delete(product);

    }

    // Exceptions Testes

    @Test
    @DisplayName("Deve lançar uma exceção quando o Id da categoria do produto não existir.")
    void deveLancarExcecaoQuandoIdDaCategoriaDoProdutoNaoExistir(){

        // Produto que sera salvo com o Id da categoria inexistente
        ProductRequestDTO dto = new ProductRequestDTO("Attack Shark X11", 1L, "Mouse Gamer com Dock", new BigDecimal("179.90"), 3);

        // Configura o mock para retornar vazio
        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.empty());

        // Verifica se a exceção correta foi lançada
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.save(dto);
        });

        // Verificar se a mensagem da exceção está correta
        assertEquals("Categoria com o id: " + dto.getCategoryId() + " não encontrada", exception.getMessage());

        // Verifica que o save NUNCA foi chamado
        verify(productRepository, never()).save(any(Product.class));
        verify(categoryRepository).findById(1L);

    }

    @Test
    @DisplayName("Deve lançar uma exceção quando o Id do produto não existir.")
    void deveLancarExcecaoQuandoIdDoProdutoNaoExistir(){

        // Id que nao existe
        Long idInexistente = 1L;

        // Configura o mock para retornar vazio
        when(productRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Verifica se a exceção correta foi lançada
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(idInexistente);
        });

        // Verificar se a mensagem da exceção está correta
        assertEquals("Produto com o id: 1 não encontrado", exception.getMessage());

        // Verifica se o findById do repository foi chamado
        verify(productRepository).findById(idInexistente);

    }

    @Test
    @DisplayName("Deve lançar uma exceção quando a categoria não conter produtos.")
    void deveRetornarExcecaoQuandoCategoriaNaocConterProdutos() {

        // Id que da categoria que sera buscado
        Long categoryId = 1L;

        // Configura o mock para retornar vazio
        when(productRepository.findByCategoryId(categoryId)).thenReturn(List.of());

        // Verifica se a exceção correta foi lançada
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.findByCategory(categoryId);
        });

        // Verificar se a mensagem da exceção está correta
        assertEquals("Nenhum produto encontrado para essa categoria", exception.getMessage());

        // Verifica se o findByCategoryId do repository foi chamado
        verify(productRepository).findByCategoryId(categoryId);

    }

}