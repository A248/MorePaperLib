package space.arim.morepaperlib.adventure;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class AdventureDependenceTest {

    @Test
    public void onlyClassesInThisPackageDependOnAdventure() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("space.arim.morepaperlib");

        classes().that().resideOutsideOfPackage(getClass().getPackageName())
                .should().onlyDependOnClassesThat().resideOutsideOfPackage("net.kyori.(**)")
                .check(importedClasses);
    }
}
