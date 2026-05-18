package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Step 1 — Unit Test: mock R + mock E, real S
@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @Mock
    Repository<Integer, Product> repo;

    @Mock
    Product mockProduct;

    private ProductService service;

    @BeforeEach
    void setUp() {
        service = new ProductService(repo);
    }

    @Test
    void addProduct_callsSave() {
        service.addProduct(mockProduct);
        verify(repo).save(mockProduct);
    }

    @Test
    void findById_callsFindOne_returnsProduct() {
        when(repo.findOne(1)).thenReturn(mockProduct);
        Product result = service.findById(1);
        assertEquals(mockProduct, result);
        verify(repo).findOne(1);
    }

    @Test
    void getAllProducts_callsFindAll_returnsList() {
        when(repo.findAll()).thenReturn(List.of(mockProduct));
        List<Product> result = service.getAllProducts();
        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void deleteProduct_callsDelete() {
        service.deleteProduct(1);
        verify(repo).delete(1);
    }
}
