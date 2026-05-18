package drinkshop.service;

import drinkshop.domain.Product;
import drinkshop.repository.AbstractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Step 2 — Integration Test: real R + mock E
@ExtendWith(MockitoExtension.class)
class ProductServiceIntegrationStep2Test {

    static class InMemoryProductRepo extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product e) { return e.getId(); }
    }

    @Mock
    Product mockProduct1;

    @Mock
    Product mockProduct2;

    private ProductService service;
    private InMemoryProductRepo repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryProductRepo();
        service = new ProductService(repo);
    }

    @Test
    void addProduct_mockProduct_savedInRealRepo() {
        when(mockProduct1.getId()).thenReturn(1);
        service.addProduct(mockProduct1);
        assertEquals(mockProduct1, service.findById(1));
    }

    @Test
    void addProduct_twoMockProducts_repoContainsBoth() {
        when(mockProduct1.getId()).thenReturn(1);
        when(mockProduct2.getId()).thenReturn(2);
        service.addProduct(mockProduct1);
        service.addProduct(mockProduct2);
        assertEquals(2, service.getAllProducts().size());
    }
}
