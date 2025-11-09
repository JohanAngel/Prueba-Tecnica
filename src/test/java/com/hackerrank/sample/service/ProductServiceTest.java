package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.FilterDto;
import com.hackerrank.sample.dto.ProductDto;
import com.hackerrank.sample.exception.BadResourceRequestException;
import com.hackerrank.sample.mapper.ProductMapper;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private IProductRepository iProductRepository;
    private ProductMapper productMapper;
    private ProductService productService;
    private Product product;

    /**
     * Configuración previa a cada test:
     * - Inicializa los mocks.
     * - Crea una instancia de ProductService.
     * - Crea un objeto Product base para las pruebas.
     */

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productMapper = new ProductMapper();
        productService = new ProductService(iProductRepository, productMapper);

        product = new Product();
        product.setId(1L);
        product.setName("Balon Blanco");
        product.setDescription("Balon de futbol");
        product.setPrice(10000.0);
        product.setRating(5.0);
        product.setSpecifications("Prueba");
    }

    /**
     * Prueba: obtener todos los productos.
     *
     * Escenario:
     * - Se simula que el repositorio devuelve una lista con un producto.
     *
     * Verifica:
     * - Que la lista no sea nula.
     * - Que contenga un solo producto.
     * - Que el nombre del producto coincida.
     * - Que el método getAll() del repositorio se llame una vez.
     */

    @Test
    void testGetAll() {
        when(iProductRepository.getAll()).thenReturn(Arrays.asList(product));

        List<Product> result = productService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Balon Blanco", result.get(0).getName());
        verify(iProductRepository, times(1)).getAll();
    }

    /**
     * Prueba: obtener un producto por su ID.
     *
     * Escenario:
     * - Se simula que el repositorio devuelve el producto buscado.
     *
     * Verifica:
     * - Que el resultado no sea nulo.
     * - Que el nombre del producto sea el esperado.
     * - Que el método findById() del repositorio se invoque correctamente.
     */

    @Test
    void testFindById() {
        when(iProductRepository.findById(1L)).thenReturn(product);

        Product result = productService.getById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Balon Blanco", result.getName());
        verify(iProductRepository, times(1)).findById(1L);
    }

    /**
     * Prueba: filtrar productos por nombre.
     *
     * Escenario:
     * - Se simula una lista con un producto.
     * - Se define un filtro por nombre ("Blanco").
     *
     * Verifica:
     * - Que el filtro se aplique correctamente y devuelva el producto esperado.
     */

    @Test
    void testFilterByName() {
        when(iProductRepository.getAll()).thenReturn(List.of(product));

        FilterDto filter = new FilterDto();
        filter.setKey("name");
        filter.setValue("Blanco");

        List<Product> result = productService.filterProducts(List.of(filter));

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Balon Blanco", result.get(0).getName());
    }

    /**
     * Prueba: filtrar productos usando una columna inválida.
     *
     * Escenario:
     * - Se pasa un filtro con una clave inexistente.
     *
     * Verifica:
     * - Que se lance una excepción {@link BadResourceRequestException}.
     */

    @Test
    void testFilterProductsInvalidField() {
        when(iProductRepository.getAll()).thenReturn(List.of(product));

        FilterDto filter = new FilterDto();
        filter.setKey("columnaInvalida");
        filter.setValue("valor");

        Assertions.assertThrows(BadResourceRequestException.class, () ->
                productService.filterProducts(List.of(filter))
        );
    }

    /**
     * Prueba: creación de un nuevo producto.
     *
     * Escenario:
     * - No existen productos previamente (lista vacía).
     * - Se crea un {@link ProductDto} con la información del nuevo producto.
     *
     * Verifica:
     * - Que el producto resultante no sea nulo.
     * - Que los datos coincidan con los del DTO.
     * - Que se haya asignado un ID automáticamente.
     * - Que el método save() del repositorio se haya invocado.
     */

    @Test
    void testCreateProduct() {
        ProductDto dto = new ProductDto();
        dto.setName("Balón Blanco");
        dto.setDescription("Balón de fútbol");
        dto.setPrice(10000.0);
        dto.setRating(4.0);
        dto.setImage("imagen.png");
        dto.setSpecifications("Tamaño 5");

        when(iProductRepository.getAll()).thenReturn(new ArrayList<>());
        Product result = productService.create(dto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Balón Blanco", result.getName());
        Assertions.assertEquals(1L, result.getId());
        verify(iProductRepository, times(1)).save(anyList());
    }

    /**
     * Prueba: eliminación exitosa de un producto.
     *
     * Escenario:
     * - Se simula la existencia de un producto con ID 1.
     * - Se llama al método delete().
     *
     * Verifica:
     * - Que el método devuelva el ID eliminado.
     * - Que se invoquen los métodos findById() y delete() del repositorio.
     */

    @Test
    void testDeleteProduct_Success() {
        when(iProductRepository.findById(1L)).thenReturn(product);

        Long result = productService.delete(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result);
        verify(iProductRepository, times(1)).findById(1L);
        verify(iProductRepository, times(1)).delete(1L);
    }

}
