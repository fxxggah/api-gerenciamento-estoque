package com.estoque.gerenciamentoestoque.controller;

import com.estoque.gerenciamentoestoque.dto.product.ProductRequestDTO;
import com.estoque.gerenciamentoestoque.dto.product.ProductResponseDTO;
import com.estoque.gerenciamentoestoque.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos") // Agrupa no Swagger
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Cadastrar um produto", description = "Cria um novo produto no sistema a partir dos dados fornecidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria informada não encontrada")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(dto));
    }

    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista contendo todos os produtos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto específico baseado no ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok((productService.findById(productId)));
    }

    @Operation(summary = "Listar produtos por categoria", description = "Retorna todos os produtos vinculados a uma categoria específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> findByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(productService.findByCategory(categoryId));
    }

    @Operation(summary = "Atualizar um produto", description = "Atualiza os dados de um produto existente através do ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable("productId") Long productId, @Valid @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.update(productId, dto));
    }

    @Operation(summary = "Excluir um produto", description = "Remove permanentemente um produto do sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
