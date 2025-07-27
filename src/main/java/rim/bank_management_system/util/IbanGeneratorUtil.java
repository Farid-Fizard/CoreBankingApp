package rim.bank_management_system.util;

import org.springframework.stereotype.Component;

import java.util.Random;
@Component
public class IbanGeneratorUtil {

    // Azərbaycan bank sisteminə uyğun nümunə İBAN: AZ12NABZ00000000000000000001
    public String generateAzerbaijanIban() {
        String countryCode = "AZ";
        String bankCode = "NABZ";
        String accountNumber = String.format("%020d", new Random().nextLong() & Long.MAX_VALUE);
        return countryCode + "12" + bankCode + accountNumber;
    }
}

