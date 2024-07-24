package au.com.connectid.taikaidemo;

import com.enofex.taikai.Taikai;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.EnumSet;

import static com.tngtech.archunit.core.domain.JavaModifier.FINAL;
import static com.tngtech.archunit.core.domain.JavaModifier.PRIVATE;

public class ArchitectureTest {

    @Test
    void shouldFulfillConstraints() {
        Taikai.builder()
                .namespace("au.com.connectid.taikaidemo")
                .failOnEmpty(true)
                .java(java -> java
                        .noUsageOfDeprecatedAPIs()
                        .methodsShouldNotDeclareGenericExceptions()
                        .utilityClassesShouldBeFinalAndHavePrivateConstructor()
                        .imports(imports -> imports
                                .shouldHaveNoCycles()
                                .shouldNotImport("..shaded..")
                                .shouldNotImport("org.junit.."))
                        .naming(naming -> naming
                                .classesShouldNotMatch(".*Impl")
                                .methodsShouldNotMatch("foo")
                                .fieldsShouldNotMatch("bar")
                                .fieldsShouldMatch("com.awesome.Foo", "foo")
                                .constantsShouldFollowConventions()
                                .interfacesShouldNotHavePrefixI()))
                .logging(logging -> logging
                        .loggersShouldFollowConventions(Logger.class, "logger", EnumSet.of(PRIVATE, FINAL)))
                .test(test -> test
                        .junit5(junit5 -> junit5
                                .classesShouldNotBeAnnotatedWithDisabled()
                                .methodsShouldNotBeAnnotatedWithDisabled()))
                .spring(spring -> spring
                        .noAutowiredFields()
                        .boot(boot -> boot
                                .springBootApplicationShouldBeIn("au.com.connectid.auditlogging"))
                        .configurations(configuration -> configuration
                                .namesShouldEndWithConfiguration())
                        .controllers(controllers -> controllers
                                .shouldBeAnnotatedWithRestController()
                                .namesShouldEndWithController()
                                .shouldNotDependOnOtherControllers()
                                .shouldBePackagePrivate())
                        .services(services -> services
                                .shouldBeAnnotatedWithService()
                                .shouldNotDependOnControllers()
                                .namesShouldEndWithService())
                        .repositories(repositories -> repositories
                                .shouldBeAnnotatedWithRepository()
                                .shouldNotDependOnServices()
                                .namesShouldEndWithRepository()))
                .build()
                .check();
    }
}