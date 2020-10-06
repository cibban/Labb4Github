package kyh.labs.lab4;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TollFeeCalculator {

    TollFeeCalculator() {
        // Dummy constructor to enable test.
    }

    public static void calculateTollFee(String inputFile) { // -> Metod istället för att instantiera klassen.
        try {
            Scanner sc = new Scanner(new File(inputFile));
            String[] dateStrings = sc.nextLine().split(", ");
            LocalDateTime[] dates = new LocalDateTime[dateStrings.length]; // Length -1 -> length
            for(int i = 0; i < dates.length; i++) {
                dates[i] = LocalDateTime.parse(dateStrings[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            System.out.println("The total fee for the input file is: " + getTotalFeeCost(dates));
            sc.close(); //Stäng filen.
        } catch(IOException e) {
            System.err.println("Could not read file " + inputFile);
        } catch(NoSuchElementException elementException) {
            System.err.println("File " + inputFile + " is empty."); // Hantering av tomma filer fanns inte.
        }
    }

    public static int getTotalFeeCost(LocalDateTime[] dates) {
        int totalFee = 0;
        LocalDateTime intervalStart = dates[0];
        int intervalMaxFee = 0; // -> För att inte bara de senaste två värdena ska jämföras.
        long diffInMinutes;

        for(int i = 0; i < dates.length; i++) { // Foreach -> Fori.
            // Hantering av filer med mer än ett datum.
            if(!intervalStart.truncatedTo(ChronoUnit.DAYS).equals(dates[i].truncatedTo(ChronoUnit.DAYS))){
                totalFee += intervalMaxFee; // Maxpriset från förra intervallet läggs till totalen innan man bryter.
                System.err.println("Warning: File contains more than one date.");
                break;
            }

            System.out.println(dates[i].toString());
            diffInMinutes = intervalStart.until(dates[i], ChronoUnit.MINUTES);

            if(diffInMinutes > 60) {
                totalFee += intervalMaxFee; // Maxpriset från förra intervallet läggs till totalen innan nästa pris hämtas.
                intervalStart = dates[i];
                intervalMaxFee = getTollFeePerPassing(dates[i]); // <-
                if(i == dates.length-1) totalFee += intervalMaxFee; // Maxpriset för intervallet  läggs till totalen om det är sista raden.
            } else {
                intervalMaxFee = Math.max(getTollFeePerPassing(dates[i]), intervalMaxFee);
                if(i == dates.length-1) totalFee += intervalMaxFee; // Maxpriset för intervallet  läggs till totalen om det är sista raden.
            }
        }
        return Math.min(totalFee, 60);  // -> Min istället för max.
    }

    public static int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        if (hour == 6 && minute <= 29) return 8;
        else if (hour == 6) return 13;
        else if (hour == 7) return 18;
        else if (hour == 8 && minute <= 29) return 13;
        else if (hour <= 14) return 8; // -> kolla hela timmar, inte bara de första 30 minuterna.
        else if (hour == 15 && minute <= 29) return 13;
        else if (hour == 15 || hour == 16) return 18; // -> ta inte betalt mellan 15 och 15:29.
        else if (hour == 17) return 13;
        else if (hour == 18 && minute <= 29) return 8;
        else return 0;
    }

    public static boolean isTollFreeDate(LocalDateTime date) {
        return date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7 || date.getMonth().getValue() == 7;
    }

    public static void main(String[] args) {
        calculateTollFee("testData/Lab4.txt");
    }
}
