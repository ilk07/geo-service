import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SendMessageTest {

    @BeforeAll
    public static void startLocalizationServiceTest(){
        System.out.println("\n---START SEND MESSAGE TESTS---");
    }

    @AfterEach
    void afterSendMessageTest(TestInfo testInfo) {
        System.out.println(". Test \"" + testInfo.getDisplayName() + "\" сomplete.");
    }

    @ParameterizedTest(name = "Send message for IP {0} in {2}")
    @MethodSource("sendSource")
    void messageSendTest(String ip, Location location, Country country, String message){
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(ip)).thenReturn(location);

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(country)).thenReturn(message);

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        String result = messageSender.send(headers);

        Assertions.assertEquals(message, result);

    }

    public static Stream<Arguments> sendSource(){
        return Stream.of(
                Arguments.of(GeoServiceImpl.LOCALHOST,  new Location(null, null, null, 0), null, "Добро пожаловать"),
                Arguments.of(GeoServiceImpl.MOSCOW_IP,  new Location("Moscow", Country.RUSSIA, "Lenina", 15), Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(GeoServiceImpl.NEW_YORK_IP, new Location("New York", Country.USA, " 10th Avenue", 32),  Country.USA, "Welcome"),
                Arguments.of("172.0.0.0", new Location("Moscow", Country.RUSSIA, null, 0), Country.RUSSIA, "Добро пожаловать"),
                Arguments.of("45.6.101.128", new Location("Rio de Janeiro", Country.BRAZIL, null, 0), Country.BRAZIL, "Welcome"),
                Arguments.of("96.0.0.0", new Location("New York", Country.USA, null, 0), Country.USA, "Welcome")
        );
    }

}
