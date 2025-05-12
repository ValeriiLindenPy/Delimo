package rs.delimo;

import com.tngtech.archunit.core.domain.JavaClass;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static org.junit.jupiter.api.Assertions.*;

class DelimoApplicationTest {

    @Test
    void applicationModulesTest() {
        var modules = ApplicationModules.of(
                DelimoApplication.class,
                // exclude non-modules packages
                JavaClass.Predicates.resideInAnyPackage(
                        "rs.delimo.common..",
                        "rs.delimo.api..","rs.delimo.config.."
                )
        ).verify();

        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }

}