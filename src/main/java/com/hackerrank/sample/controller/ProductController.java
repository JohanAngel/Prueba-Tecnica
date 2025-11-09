package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.FilterDto;
import com.hackerrank.sample.dto.ProductDto;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<?> AllProduct(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.getAll());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterProducts(@RequestBody List<FilterDto> filters) {
        try {
            return ResponseEntity.ok(productService.filterProducts(filters));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> productById(@PathVariable Long id){
        try{
            Product product  = productService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDto productDto){
        try{
            Product newProduct = productService.create(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDto productDto) {
        try {
            Product updated = productService.update(id, productDto);
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(productService.delete(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
