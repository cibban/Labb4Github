package kyh.labs.lab4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Toll Fee Calculator")
public class TollFeeCalcTests {

    @Test
    @DisplayName("Testing that the unit class is not instantiated with parameters.")
    public void testMethodNotInstance() {
        //noinspection InstantiationOfUtilityClass
        new TollFeeCalculator();
        // Throws exception if old method is used.
    }


    @Test
    @DisplayName("Testing that all dates are read from file.")
    void testReadAllDatesInFile() {
        TollFeeCalculator.calculateTollFee("./testData/lengthTest.txt");
//        File has only one date. Test throws exception if reading fewer.
    }

    @Test
    @DisplayName("Test proper handling of empty files.")
    void testHandleEmptyFile() {
        TollFeeCalculator.calculateTollFee("./testData/empty.txt");
//      Throws exception if exception is not caught.
    }

    @Test
    @DisplayName("Testing that files with more than one date are handled correctly.")
    void testBreakOnMutlipleDatesInFile() {
        LocalDateTime[] dates = new LocalDateTime[6];
        dates[0] = LocalDateTime.of(2020, 10, 1, 9, 10);
        dates[1] = LocalDateTime.of(2020, 10, 1, 10, 12);
        dates[2] = LocalDateTime.of(2020, 10, 1, 11, 14);
        dates[3] = LocalDateTime.of(2020, 10, 1, 12, 16);
        dates[4] = LocalDateTime.of(2020, 10, 2, 13, 14);
        dates[5] = LocalDateTime.of(2020, 10, 3, 14, 15);

        assertEquals(32, TollFeeCalculator.getTotalFeeCost(dates));
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
    @DisplayName("Testing that the 60 minute rule works") // Detta var två buggar som "hjälptes åt".
    void testSixtyMinuteRule() {
        LocalDateTime[] dates = new LocalDateTime[4];
        dates[0] = LocalDateTime.of(2020, 10, 1, 6, 20);
        dates[1] = LocalDateTime.of(2020, 10, 1, 6, 40);
        dates[2] = LocalDateTime.of(2020, 10, 1, 7, 20);
        dates[3] = LocalDateTime.of(2020, 10, 1, 9, 40);

        assertEquals(26, TollFeeCalculator.getTotalFeeCost(dates));
    }

    @Test
    @DisplayName("Testing that the 60 kr max price is invoked when it should.")
    void testMaxPrice() {
        LocalDateTime[] datesMoreThanMax = new LocalDateTime[4];
        datesMoreThanMax[0] = LocalDateTime.of(2020, 10, 1, 6, 30);
        datesMoreThanMax[1] = LocalDateTime.of(2020, 10, 1, 7, 35);
        datesMoreThanMax[2] = LocalDateTime.of(2020, 10, 1, 15, 0);
        datesMoreThanMax[3] = LocalDateTime.of(2020, 10, 1, 16, 12);

        assertEquals(60, TollFeeCalculator.getTotalFeeCost(datesMoreThanMax));
    }

    @Test
    @DisplayName("Testing that the 60 kr max price is not invoked when it shouldn't.")
    void testNotMaxPrice() {

        LocalDateTime[] datesLessThanMax = new LocalDateTime[4];
        datesLessThanMax[0] = LocalDateTime.of(2020, 10, 1, 6, 0);
        datesLessThanMax[1] = LocalDateTime.of(2020, 10, 1, 8, 32);
        datesLessThanMax[2] = LocalDateTime.of(2020, 10, 1, 15, 6);
        datesLessThanMax[3] = LocalDateTime.of(2020, 10, 1, 18, 0);

        assertEquals(37, TollFeeCalculator.getTotalFeeCost(datesLessThanMax));

    }

    @Test
    @DisplayName("Testing that free dates works.")  // Överflödigt för uppgiften men ska man ändå kolla allt så...
    void testTollFreeDate() {
        assertTrue(TollFeeCalculator.isTollFreeDate(LocalDateTime.of(2020, 10, 3, 17, 33)));
        assertFalse(TollFeeCalculator.isTollFreeDate(LocalDateTime.of(2020, 10, 1, 18, 10)));
    }

    @Test
    @DisplayName("Testing that the file is closed.")
    void testFileClosed() {
        TollFeeCalculator.calculateTollFee("./testData/Lab4.txt");
        boolean fileClosed = true; // Vi lyckades inte hitta ett sätt att testa om filen är öppen eller stängd. Feedback vore välkommet.
        assertTrue(fileClosed);
    }


}
