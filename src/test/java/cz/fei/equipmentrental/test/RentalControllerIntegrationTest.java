package cz.fei.equipmentrental.test;

import cz.fei.equipmentrental.entity.Equipment;
import cz.fei.equipmentrental.entity.User;
import cz.fei.equipmentrental.repository.EquipmentRepository;
import cz.fei.equipmentrental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RentalControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private User testUser;
    private Equipment testEquipment;

    @BeforeEach
    void setUp() {
        // Protože test běží jako reálný server, vyčistíme DB manuálně před každým testem
        userRepository.deleteAll();
        equipmentRepository.deleteAll();

        testUser = userRepository.save(new User("Jan Novák", "jan.novak@test.cz"));
        testEquipment = equipmentRepository.save(new Equipment("Míchačka", new BigDecimal("500.00")));
    }

    @Test
    void shouldCreateRentalSuccessfully() {
        String jsonRequest = """
                {
                    "userId": %d,
                    "equipmentId": %d,
                    "startDate": "%s",
                    "endDate": "%s"
                }
                """.formatted(
                testUser.getId(),
                testEquipment.getId(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity("/api/rentals", request, Map.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Výpůjčka byla úspěšně vytvořena.", response.getBody().get("message"));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        String jsonRequest = """
                {
                    "equipmentId": %d,
                    "startDate": "2020-01-01",
                    "endDate": "2020-01-05"
                }
                """.formatted(testEquipment.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity("/api/rentals", request, Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ID uživatele je povinné.", response.getBody().get("userId"));
        assertEquals("Datum začátku nesmí být v minulosti.", response.getBody().get("startDate"));
    }
}