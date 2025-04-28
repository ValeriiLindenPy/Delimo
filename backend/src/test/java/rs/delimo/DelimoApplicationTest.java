package rs.delimo;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

import static org.junit.jupiter.api.Assertions.*;

class DelimoApplicationTest {

    @Test
    void applicationModulesTest() {
        var modules = ApplicationModules.of(DelimoApplication.class);
        modules.verify();
        modules.forEach(System.out::println);
    }

}