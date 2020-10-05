package kyh.labs.lab4;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class TollFeeCalculator {

    public TollFeeCalculator(String inputFile) {
        try {
            Scanner sc = new Scanner(new File(inputFile));
            String[] dateStrings = sc.nextLine().split(", ");
            LocalDateTime[] dates = new LocalDateTime[dateStrings.length]; // Length -1 -> length
            for(int i = 0; i < dates.length; i++) {
                dates[i] = LocalDateTime.parse(dateStrings[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            System.out.println("The total fee for the input file is: " + getTotalFeeCost(dates));
        } catch(IOException e) {
            System.err.println("Could not read file " + inputFile);
        }
    }

    public static int getTotalFeeCost(LocalDateTime[] dates) {
        int totalFee = 0;
        LocalDateTime intervalStart = dates[0];
        int intervalMaxFee = 0;
        long diffInMinutes;

        for(int i = 0; i < dates.length; i++) {
            System.out.println(dates[i].toString());
            diffInMinutes = intervalStart.until(dates[i], ChronoUnit.MINUTES);

            if(diffInMinutes > 60) {
                totalFee += intervalMaxFee;
                totalFee += getTollFeePerPassing(dates[i]);
                intervalStart = dates[i];
                intervalMaxFee = 0;
            } else {
                intervalMaxFee = Math.max(getTollFeePerPassing(dates[i]), getTollFeePerPassing(intervalStart));
                if(i == dates.length-1) totalFee += intervalMaxFee;
            }
        }
        return Math.min(totalFee, 60);  // Ska vara min istället för max.
    }

    public static int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        if (hour == 6 && minute <= 29) return 8; // Kontroll >=0 överflödig.
        else if (hour == 6) return 13; // Kontroll <=59 överflödig.
        else if (hour == 7) return 18; // Kontroll av minuter överflödig.
        else if (hour == 8 && minute <= 29) return 13; // Kontroll >=0 överflödig.
        else if (hour <= 14) return 8; // Kontroll av minuter överflödig.
        else if (hour == 15 && minute <= 29) return 13; // Kontroll >=0 överflödig.
        else if (hour == 15 || hour == 16) return 18; // Kontroll av minuter överflödiga eftersom "else"
        else if (hour == 17) return 13; // Kontroll av minuter överflödig.
        else if (hour == 18 && minute <= 29) return 8; // Kontroll >=0 överflödig.
        else return 0;
    }

    public static boolean isTollFreeDate(LocalDateTime date) {
        return date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7 || date.getMonth().getValue() == 7;
    }

    public static void main(String[] args) {
        new TollFeeCalculator("testData/Lab4.txt");
    }
}
