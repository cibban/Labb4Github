package kyh.labs.lab4;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Toll Fee Calculator")
public class TollFeeCalcTests {
    public TollFeeCalculator tFC = new TollFeeCalculator("testData/Lab4.txt");

    @Test
    @DisplayName("Testing that all dates are read.")
    void testReadInputFileCorrectly() {
        new TollFeeCalculator("./testData/lengthTest.txt");
//        File has only one date. Test throws exception if reading fewer.
    }

    @Test
    @DisplayName("Testing that the 60 minute rule works")
    void testSixtyMinuteRule() {
        LocalDateTime[] dates = new LocalDateTime[4];
        dates[0] = LocalDateTime.of(2020, 10, 01, 6, 20);
        dates[1] = LocalDateTime.of(2020, 10, 01, 6, 40);
        dates[2] = LocalDateTime.of(2020, 10, 01, 7, 21);
        dates[3] = LocalDateTime.of(2020, 10, 01, 9, 40);

        assertEquals(39, TollFeeCalculator.getTotalFeeCost(dates));
    }

    @Test
    @DisplayName("Testing that the 60 kr max price works.")
    void testMaxPrice() {
        LocalDateTime[] datesMoreThanMax = new LocalDateTime[4];
        datesMoreThanMax[0] = LocalDateTime.of(2020, 10, 01, 6, 30);
        datesMoreThanMax[1] = LocalDateTime.of(2020, 10, 01, 7, 35);
        datesMoreThanMax[2] = LocalDateTime.of(2020, 10, 01, 15, 00);
        datesMoreThanMax[3] = LocalDateTime.of(2020, 10, 01, 16, 12);

        assertEquals(60, TollFeeCalculator.getTotalFeeCost(datesMoreThanMax));

        LocalDateTime[] datesLessThanMax = new LocalDateTime[4];
        datesLessThanMax[0] = LocalDateTime.of(2020, 10, 01, 6, 00);
        datesLessThanMax[1] = LocalDateTime.of(2020, 10, 01, 8, 32);
        datesLessThanMax[2] = LocalDateTime.of(2020, 10, 01, 15, 06);
        datesLessThanMax[3] = LocalDateTime.of(2020, 10, 01, 18, 00);

        assertEquals(37, TollFeeCalculator.getTotalFeeCost(datesLessThanMax));

    }


    @Test
    @DisplayName("Testing that free dates works.")
    void testTollFreeDate() {
        assertTrue(tFC.isTollFreeDate(LocalDateTime.of(2020, 10, 03, 17, 33)));
        assertFalse(tFC.isTollFreeDate(LocalDateTime.of(2020, 10, 01, 18, 10)));
    }

    void exceptionTesting() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("a message");
        });
        assertEquals("a message", exception.getMessage());
    }
}
