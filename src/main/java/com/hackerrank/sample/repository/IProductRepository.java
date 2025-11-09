package com.hackerrank.sample.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import org.springframework.stereotype.Repository;

import java.io.DataOutput;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IProductRepository {

    static final ObjectMapper mapper = new ObjectMapper();
    static final String FILE_PATH = "products.json";

    public List<Product> getAll() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
                mapper.writeValue(file, new ArrayList<>());
            }
            List<Product> products = mapper.readValue(file, new TypeReference<List<Product>>() {});
            return products != null ? products : new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo", e);
        }
    }

    public Product findById(Long productId){
        return getAll()
                .stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NoSuchResourceFoundException(String.format("Producto con ID %d no encontrado", productId)));
    }

    public void save(List<Product> products){
        try{
            File file = new File(FILE_PATH);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, products);
        }catch (Exception e){
            throw new RuntimeException("Error al guardar el archivo");
        }
    }

    public Long delete(Long productId){
        try {
            List<Product> products = getAll();
            products.removeIf(product -> product.getId().equals(productId));
            save(products);
            return productId;
        }catch (Exception e){
            throw new RuntimeException(String.format("No se encontro el producto ID %d", productId));
        }
    }
}

