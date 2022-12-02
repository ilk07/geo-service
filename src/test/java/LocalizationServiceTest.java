import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationServiceImpl;

public class LocalizationServiceTest {

    @BeforeAll
    public static void startLocalizationServiceTest(){
        System.out.println("\n---START LOCALIZATION SERVICE TESTS---");
    }

    @AfterEach
    void afterLocalizationServiceTest(TestInfo testInfo) {
        System.out.println(" Test \"" + testInfo.getDisplayName() + "\" сomplete.");
    }

    @ParameterizedTest(name = "Get localization for Country {0}")
    @EnumSource(Country.class)
    @DisplayName("Get localization by country")
    public void localizationServiceImplLocaleTest(Country country){
        //given
        LocalizationServiceImpl localizationService = new LocalizationServiceImpl();

        //when
        String result = localizationService.locale(country);
        String expected = country.name().equals("RUSSIA") ? "Добро пожаловать" : "Welcome";

        //then
        Assertions.assertEquals(expected, result);
    }
}
