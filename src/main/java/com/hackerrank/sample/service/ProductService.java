package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.FilterDto;
import com.hackerrank.sample.dto.ProductDto;
import com.hackerrank.sample.exception.BadResourceRequestException;
import com.hackerrank.sample.mapper.ProductMapper;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class ProductService {

    private final IProductRepository iProductRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(IProductRepository iProductRepository, ProductMapper productMapper) {
        this.iProductRepository = iProductRepository;
        this.productMapper = productMapper;
    }

    /**
     * Filtra los productos según una lista de filtros dinámicos.
     *
     * Cada filtro especifica un campo del modelo {@link Product} y un valor que se debe comparar.
     * Los filtros se aplican de forma acumulativa (AND lógico).
     *
     * @param filters Lista de filtros a aplicar (clave y valor).
     * @return Lista de productos que cumplen con todos los filtros.
     * @throws BadResourceRequestException Si algún filtro tiene campos inválidos o vacíos.
     */

    public List<Product> filterProducts(List<FilterDto> filters) {
        List<Product> allProducts = iProductRepository.getAll();

        return allProducts.stream()
                .filter(product -> filters.stream().allMatch(filter -> {
                    String key = filter.getKey();
                    String value = filter.getValue();

                    if (key.isEmpty() || value.isEmpty()) {
                        throw new BadResourceRequestException("Debe enviar al menos un filtro válido");
                    }

                    try {
                        Field field = Product.class.getDeclaredField(key);
                        field.setAccessible(true);
                        Object fieldValue = field.get(product);

                        if (fieldValue instanceof Number) {
                            double filterValue = Double.parseDouble(value);
                            return Double.compare(((Number) fieldValue).doubleValue(), filterValue) == 0;
                        } else {
                            return fieldValue.toString().toLowerCase().contains(value.toLowerCase());
                        }

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new BadResourceRequestException("Columna inválida: " + key);
                    }
                }))
                .toList();
    }

    /**
     * Obtiene la lista completa de productos registrados.
     *
     * @return Lista de productos.
     * @throws RuntimeException Si ocurre un error en la obtención de datos.
     */

    public List<Product> getAll(){
        try{
            return iProductRepository.getAll();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Obtiene un producto específico por su identificador.
     *
     * @param productId Identificador del producto.
     * @return El producto correspondiente al ID.
     * @throws RuntimeException Si el producto no existe o hay un error en el repositorio.
     */

    public Product getById(Long productId){
        try {
            return iProductRepository.findById(productId);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * Crea un nuevo producto y lo guarda en el repositorio.
     *
     * El ID se genera automáticamente con base en el valor máximo existente.
     *
     * @param newProduct DTO con los datos del nuevo producto.
     * @return El producto creado, con ID asignado.
     * @throws BadResourceRequestException Si el DTO es nulo o ocurre un error durante la creación.
     */

    public Product create(ProductDto newProduct) {
        try {
            if (newProduct == null) {
                throw new BadResourceRequestException("El producto no puede ser nulo");
            }

            Product product = productMapper.toEntity(newProduct);
            List<Product> products = iProductRepository.getAll();

            Long productId = products.stream()
                    .mapToLong(Product::getId)
                    .max()
                    .orElse(0) + 1;

            product.setId(productId);
            products.add(product);
            iProductRepository.save(products);
            return product;
        } catch (Exception e) {
            throw new BadResourceRequestException(e.getMessage());
        }
    }

    /**
     * Actualiza la información de un producto existente.
     *
     * Proceso:
     * <ol>
     *     <li>Busca el producto por ID.</li>
     *     <li>Lo elimina temporalmente del repositorio.</li>
     *     <li>Actualiza sus datos con los valores del DTO.</li>
     *     <li>Lo agrega nuevamente a la lista y guarda los cambios.</li>
     * </ol>
     *
     * @param productId  ID del producto a actualizar.
     * @param productDto DTO con los nuevos datos del producto.
     * @return El producto actualizado.
     * @throws RuntimeException Si ocurre algún error durante el proceso.
     */

    public Product update(Long productId, ProductDto productDto) {
        try {
            Product findProduct = iProductRepository.findById(productId);
            iProductRepository.delete(productId);

            List<Product> products = iProductRepository.getAll();
            findProduct.setId(productId);
            findProduct.setName(productDto.getName());
            findProduct.setDescription(productDto.getDescription());
            findProduct.setImage(productDto.getImage());
            findProduct.setPrice(productDto.getPrice());
            findProduct.setRating(productDto.getRating());
            findProduct.setSpecifications(productDto.getSpecifications());
            products.add(findProduct);

            iProductRepository.save(products);
            return findProduct;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Elimina un producto por su identificador.
     *
     * @param productId ID del producto a eliminar.
     * @return El ID del producto eliminado.
     * @throws RuntimeException Si el producto no existe o ocurre un error al eliminarlo.
     */

    public Long delete(Long productId){
        try{
            System.out.println("Eliminando el producto ID " + productId);
            Product findProduct = getById(productId);
            iProductRepository.delete(findProduct.getId());
            return productId;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
