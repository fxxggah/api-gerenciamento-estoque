package com.estoque.gerenciamentoestoque.controller;

import com.estoque.gerenciamentoestoque.dto.category.CategoryRequestDTO;
import com.estoque.gerenciamentoestoque.dto.category.CategoryResponseDTO;
import com.estoque.gerenciamentoestoque.service.CategoryService;
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
@RequestMapping("categories")
@Tag(name = "Categorias", description = "Endpoints para gerenciamento das categorias de produtos")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Criar uma categoria", description = "Cadastra uma nova categoria no banco de dados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou erro de validação")
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@Valid @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(dto));
    }

    @Operation(summary = "Listar todas as categorias", description = "Retorna uma lista de todas as categorias cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @Operation(summary = "Buscar categoria por ID", description = "Busca os detalhes de uma categoria específica pelo seu identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(categoryService.findById(categoryId));
    }

    @Operation(summary = "Deletar uma categoria", description = "Remove uma categoria do sistema através do ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "409", description = "Não é possível excluir: existem produtos vinculados a esta categoria")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }

}