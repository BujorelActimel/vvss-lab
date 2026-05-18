package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.AbstractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Step 3 — Integration Test: real R + real E, zero mocks
class ProductServiceIntegrationStep3Test {

    static class InMemoryProductRepo extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product e) { return e.getId(); }
    }

    private ProductService service;

    @BeforeEach
    void setUp() {
        service = new ProductService(new InMemoryProductRepo());
    }

    private Product latte() {
        return new Product(1, "Latte", 15.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
    }

    @Test
    void addProduct_realProduct_persistedAndRetrievable() {
        Product p = latte();
        service.addProduct(p);
        Product found = service.findById(1);
        assertNotNull(found);
        assertEquals("Latte", found.getNume());
    }

    @Test
    void deleteProduct_existingProduct_removedFromRepo() {
        service.addProduct(latte());
        service.deleteProduct(1);
        assertNull(service.findById(1));
    }

    @Test
    void getAllProducts_afterAddTwo_sizeIsTwo() {
        service.addProduct(latte());
        service.addProduct(new Product(2, "Espresso", 10.0, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC));
        assertEquals(2, service.getAllProducts().size());
    }

    @Test
    void updateProduct_changesName_updatedInRepo() {
        service.addProduct(latte());
        service.updateProduct(1, "Cappuccino", 18.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        assertEquals("Cappuccino", service.findById(1).getNume());
    }
}
