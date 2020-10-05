package kyh.labs.lab4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Toll Fee Calculator")
public class TollFeeCalcTests {


    @Test
    @DisplayName("Testing that all dates are read.")
    void testReadInputFileCorrectly() {
            TollFeeCalculator.tollFeeCalculator("./testData/lengthTest.txt");
//        File has only one date. Test throws exception if reading fewer.
    }

    @Test
    @DisplayName("Testing that the full hour between 8:30 and 14:59 is charged, not only the last half of each hour.")
    void testFullHourCharge() {
        LocalDateTime[] dates = new LocalDateTime[6];
        dates[0] = LocalDateTime.of(2020, 10, 1, 9, 10);
        dates[1] = LocalDateTime.of(2020, 10, 1, 10, 11);
        dates[2] = LocalDateTime.of(2020, 10, 1, 11, 12);
        dates[3] = LocalDateTime.of(2020, 10, 1, 12, 13);
        dates[4] = LocalDateTime.of(2020, 10, 1, 13, 14);
        dates[5] = LocalDateTime.of(2020, 10, 1, 14, 15);

        assertEquals(48, TollFeeCalculator.getTotalFeeCost(dates));
    }

    @Test
    @DisplayName("Testing that 15-15:29 is not overcharged.")
    void testAvoidOverCharge() {
        LocalDateTime[] dates = new LocalDateTime[1];
        dates[0] = LocalDateTime.of(2020, 10, 1, 15, 15);

        assertEquals(13, TollFeeCalculator.getTotalFeeCost(dates));
    }


    @Test
    @DisplayName("Testing that the 60 minute rule works")
    void testSixtyMinuteRule() {
        LocalDateTime[] dates = new LocalDateTime[4];
        dates[0] = LocalDateTime.of(2020, 10, 1, 6, 20);
        dates[1] = LocalDateTime.of(2020, 10, 1, 6, 40);
        dates[2] = LocalDateTime.of(2020, 10, 1, 7, 21);
        dates[3] = LocalDateTime.of(2020, 10, 1, 9, 40);

        assertEquals(39, TollFeeCalculator.getTotalFeeCost(dates));
    }

    @Test
    @DisplayName("Testing that the 60 kr max price works.")
    void testMaxPrice() {
        LocalDateTime[] datesMoreThanMax = new LocalDateTime[4];
        datesMoreThanMax[0] = LocalDateTime.of(2020, 10, 1, 6, 30);
        datesMoreThanMax[1] = LocalDateTime.of(2020, 10, 1, 7, 35);
        datesMoreThanMax[2] = LocalDateTime.of(2020, 10, 1, 15, 0);
        datesMoreThanMax[3] = LocalDateTime.of(2020, 10, 1, 16, 12);

        assertEquals(60, TollFeeCalculator.getTotalFeeCost(datesMoreThanMax));

        LocalDateTime[] datesLessThanMax = new LocalDateTime[4];
        datesLessThanMax[0] = LocalDateTime.of(2020, 10, 1, 6, 0);
        datesLessThanMax[1] = LocalDateTime.of(2020, 10, 1, 8, 32);
        datesLessThanMax[2] = LocalDateTime.of(2020, 10, 1, 15, 6);
        datesLessThanMax[3] = LocalDateTime.of(2020, 10, 1, 18, 0);

        assertEquals(37, TollFeeCalculator.getTotalFeeCost(datesLessThanMax));

    }

    @Test
    @DisplayName("Testing that free dates works.")
    void testTollFreeDate() {
        assertTrue(TollFeeCalculator.isTollFreeDate(LocalDateTime.of(2020, 10, 3, 17, 33)));
        assertFalse(TollFeeCalculator.isTollFreeDate(LocalDateTime.of(2020, 10, 1, 18, 10)));
    }

}
