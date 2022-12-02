import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class GeoServiceTest {
    GeoServiceImpl geoService;

    @BeforeAll
    public static void startGeoServiceTests(){
        System.out.println("\n---START GEO SERVICE TESTS---");
    }

    @BeforeEach
    public void initOneTest(){
        geoService = new GeoServiceImpl();
    }

    @AfterEach
    void afterGeoServiceTest(TestInfo testInfo) {
        System.out.println(" Test \"" + testInfo.getDisplayName() + "\" сomplete.");
    }

    @Test
    @DisplayName("Get location by coordinates")
    public void geoServiceImplByCoordinatesTest(){
        //when
        Executable executable = () -> {
            geoService.byCoordinates(1.1, 1.3);
        };

        //then
        assertThrowsExactly(RuntimeException.class, executable);
    }

    @ParameterizedTest(name = "Get Location by IP {0}")
    @MethodSource("ipSource")
    public void geoServiceImplByIpTest(String ip, String expected){

        //when
        Location location = geoService.byIp(ip);
        String actual = location != null  ? location.getCity() : null;

        //then
        assertEquals(actual, expected);
    }
    public static Stream<Arguments> ipSource(){
        return Stream.of(
                Arguments.of(GeoServiceImpl.LOCALHOST,  null),
                Arguments.of(GeoServiceImpl.MOSCOW_IP,  "Moscow"),
                Arguments.of(GeoServiceImpl.NEW_YORK_IP,  "New York"),
                Arguments.of("172.0.0.0", "Moscow"),
                Arguments.of("96.0.0.0", "New York"),
                Arguments.of("0.0.0.0", null)
        );
    }
}
