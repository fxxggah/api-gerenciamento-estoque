package com.estoque.gerenciamentoestoque.service;

import com.estoque.gerenciamentoestoque.dto.category.CategoryMapper;
import com.estoque.gerenciamentoestoque.dto.category.CategoryRequestDTO;
import com.estoque.gerenciamentoestoque.dto.category.CategoryResponseDTO;
import com.estoque.gerenciamentoestoque.entity.Category;
import com.estoque.gerenciamentoestoque.exception.BusinessException;
import com.estoque.gerenciamentoestoque.exception.ResourceNotFoundException;
import com.estoque.gerenciamentoestoque.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    public CategoryResponseDTO save(CategoryRequestDTO dto) {

        if (categoryRepository.existsByName(dto.getName())) {
            throw new BusinessException("Categoria já existe");
        }

        Category category = CategoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);

        return CategoryMapper.toResponse(saved);
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public CategoryResponseDTO findById(Long id) {
        return CategoryMapper.toResponse(findEntityById(id));
    }

    public void delete(Long id) {
        Category category = findEntityById(id);
        categoryRepository.delete(category);
    }

}
