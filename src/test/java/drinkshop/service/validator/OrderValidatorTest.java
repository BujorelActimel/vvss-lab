package drinkshop.service.validator;

import drinkshop.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderValidatorTest {

    private OrderValidator validator;

    @BeforeEach
    void setUp() {
        validator = new OrderValidator();
    }

    private OrderItem makeItem(int productId, int qty) {
        return new OrderItem(
                new Product(productId, "Latte", 15.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY),
                qty
        );
    }

    // TC1 — SC, P1: comanda valida, nicio exceptie
    @Test
    void validate_validOrder_noException() {
        Order order = new Order(1, List.of(makeItem(1, 2)), 30.0);
        assertDoesNotThrow(() -> validator.validate(order));
    }

    // TC2 — DC(D1=T): id = 0 => invalid
    @Test
    void validate_idZero_throwsValidationException() {
        Order order = new Order(0, List.of(makeItem(1, 1)), 15.0);
        assertThrows(ValidationException.class, () -> validator.validate(order));
    }

    // TC3 — DC(D1=T): id negativ => invalid
    @Test
    void validate_idNegative_throwsValidationException() {
        Order order = new Order(-5, List.of(makeItem(1, 1)), 15.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(order));
        assertTrue(ex.getMessage().contains("ID comanda invalid!"));
    }

    // TC4 — DC(D2=T), P3: items null => ValidationException (nu NPE — testeaza bug fix)
    @Test
    void validate_nullItems_throwsValidationException() {
        Order order = new Order(1);
        order.setItems(null);
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(order));
        assertTrue(ex.getMessage().contains("Comanda fara produse!"));
    }

    // TC5 — DC(D3=T), P4: items lista goala => ValidationException
    @Test
    void validate_emptyItems_throwsValidationException() {
        Order order = new Order(1, List.of(), 0.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(order));
        assertTrue(ex.getMessage().contains("Comanda fara produse!"));
    }

    // TC6 — DC(D5=T), P5: item cu quantity = 0 => catch in loop => ValidationException
    @Test
    void validate_itemWithZeroQuantity_throwsValidationException() {
        Order order = new Order(1, List.of(makeItem(1, 0)), 0.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(order));
        assertTrue(ex.getMessage().contains("Cantitate invalida!"));
    }

    // TC7 — DC(D6=T), P6: total negativ => ValidationException
    @Test
    void validate_negativeTotalPrice_throwsValidationException() {
        Order order = new Order(1, List.of(makeItem(1, 1)), -1.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(order));
        assertTrue(ex.getMessage().contains("Total invalid!"));
    }

    // TC8 — MCC, P7: toate conditiile invalide => mesaj compus cu toate erorile
    @Test
    void validate_allInvalid_exceptionContainsAllErrors() {
        Order order = new Order(0, List.of(), -1.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(order));
        String msg = ex.getMessage();
        assertTrue(msg.contains("ID comanda invalid!"));
        assertTrue(msg.contains("Comanda fara produse!"));
        assertTrue(msg.contains("Total invalid!"));
    }

    // TC9 — LC(2 iteratii): 2 items valide => no exception
    @Test
    void validate_twoValidItems_noException() {
        Order order = new Order(1, List.of(makeItem(1, 1), makeItem(2, 3)), 60.0);
        assertDoesNotThrow(() -> validator.validate(order));
    }

    // TC10 — LC(2 iteratii, catch): 2 items, al doilea invalid => ValidationException
    @Test
    void validate_twoItemsSecondInvalid_throwsValidationException() {
        Order order = new Order(1, List.of(makeItem(1, 2), makeItem(2, 0)), 30.0);
        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(order));
        assertTrue(ex.getMessage().contains("Cantitate invalida!"));
    }
}
