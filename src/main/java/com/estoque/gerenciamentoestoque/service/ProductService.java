package com.estoque.gerenciamentoestoque.service;

import com.estoque.gerenciamentoestoque.dto.product.ProductMapper;
import com.estoque.gerenciamentoestoque.dto.product.ProductRequestDTO;
import com.estoque.gerenciamentoestoque.dto.product.ProductResponseDTO;
import com.estoque.gerenciamentoestoque.entity.Category;
import com.estoque.gerenciamentoestoque.entity.Product;
import com.estoque.gerenciamentoestoque.exception.ResourceNotFoundException;
import com.estoque.gerenciamentoestoque.repository.CategoryRepository;
import com.estoque.gerenciamentoestoque.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponseDTO save(ProductRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria com o id: " + dto.getCategoryId() + " não encontrada"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setAmount(dto.getAmount());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return ProductMapper.toResponse(savedProduct);
    }

    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id: " + id + " não encontrado"));

        return ProductMapper.toResponse(product);
    }


    public List<ProductResponseDTO> findByCategory(Long categoryId) {

        List<Product> products = productRepository.findByCategoryId(categoryId);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum produto encontrado para essa categoria");
        }

        return products.stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product productUpdate = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id: " + id + " não encontrado"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria com o id: " + dto.getCategoryId() + " não encontrada"));

        productUpdate.setName(dto.getName());
        productUpdate.setCategory(category);
        productUpdate.setDescription(dto.getDescription());
        productUpdate.setPrice(dto.getPrice());
        productUpdate.setAmount(dto.getAmount());

        Product updated = productRepository.save(productUpdate);

        return ProductMapper.toResponse(updated);
    }

    public void delete(Long id) {
        Product oldProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id: " + id + " não encontrado"));

        productRepository.delete(oldProduct);
    }

}
