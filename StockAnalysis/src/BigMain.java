import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * This is what you have:
 * files of data in a directory called Data, in a specific path (C:/ProfOmar/)
 * the data will be in C:/ProfOmar/Data. You'll find the data of all stocks and ETFs
 * for example the data for IBM will be in IBM_Daily.csv (comma separated file)
 * String fileName = symbol + "_Daily.csv"
 * You have two special files:  stocks.txt and ETFs.txt in stocks.txt you have all
 * the symbols of the stocks you need to test 171 stocks
 * in ETFs.txt you will find all the ETFs you need to test.
 *
 * What to generate? Stats concerning stocks (for all stocks together)
 * Stats for ETFs all together, then combine the results (this one is easy)
 * We will test for 17 combinations of loss and target
 *
 */
public class BigMain {
    private static ArrayList<csvOutputBean> statsList;

    public static void main(String[] args) {
        double[] target = new double[4];
        target[0] = 0.01;
        target[1] = 0.02;
        target[2] = 0.05;
        target[3] = 0.1;
        double[] loss = new double[4];
        loss[0] = 0.1;
        loss[1] = 0.2;
        loss[2] = 0.5;
        loss[3] = 1;
        String path = "F:\\WorkSpace\\MAC286\\Data\\"; //your path here
        //if you use Linux "/home/yourhome/.../.../"
        statsList = new ArrayList<csvOutputBean>();
        for (int i =0; i<4; i++) {//test all combinations of loss and target
            for (int j =0; j<4; j++){
                Simulator sim = new Simulator(path, "stocks.txt", loss[i], target[j]);
                //First call object you create will be
                // Simulator sim = new Simulator("C:\\ProfOmar286_18\\Data\\", "stocks.txt", 0.01, 0.01);
                sim.run();
                //display the stats for these parameters loss[i], target[j]
                tradeArray Tr = sim.getTrades();
                //print the stats
                System.out.println("Stocks: " + loss[i]+", " + target[j] + "\n" + Tr.getStats().toString());
                //statsList.add(new csvOutputBean("Stocks", loss[i], target[j], Tr.getStats()));
                Simulator simETF = new Simulator(path, "ETFs.txt", loss[i], target[j]);
                        simETF.run();
                //display the stats for these parameters loss[i], target[j]
                tradeArray Tr1 = simETF.getTrades();
                //print the stats
                System.out.println("ETFs: " + loss[i]+", " + target[j] + "\n" + Tr1.getStats().toString());
                //statsList.add(new csvOutputBean("ETFs", loss[i], target[j], Tr1.getStats()));
                //Print stats for the combination add the trades together
                Tr.addArray(Tr1);
                System.out.println("Total: " + loss[i]+", " + target[j] + "\n" + Tr.getStats().toString());
                statsList.add(new csvOutputBean("Total", loss[i], target[j], Tr.getStats()));
            }
        }
        //we need to test what if we exit the trade the same day as we enter it at the close
        //we call that loss=0, target = 0
        Simulator sim = new Simulator(path, "stocks.txt", 0.0, 0.0);
        sim.run();
        //display the stats for these parameters loss[i], target[j]
        tradeArray Tr = sim.getTrades();
        //print the stats
        System.out.println("Stocks: " + 0.0+", " + 0.0 + "\n" + Tr.getStats().toString());
        //statsList.add(new csvOutputBean("Stocks", 0.0, 0.0, Tr.getStats()));
        Simulator simETF = new Simulator(path, "ETFs.txt", 0.0, 0.0);
        simETF.run();
        //display the stats for these parameters loss[i], target[j]
        tradeArray Tr1 = simETF.getTrades();
        //print the stats
        System.out.println("ETFs: " + 0.0+", " + 0.0 +"\n"+ Tr1.getStats().toString());
        //statsList.add(new csvOutputBean("ETFs", 0.0, 0.0, Tr1.getStats()));
        //Print stats for the combination add the trades together
        Tr.addArray(Tr1);
        System.out.println("Total: " + 0.0+", " + 0.0 + "\n" + Tr.getStats().toString());
        statsList.add(new csvOutputBean("Total", 0.0, 0.0, Tr.getStats()));
        toCVS(statsList);
    }

    public static void toCVS(ArrayList<csvOutputBean> list){
        String headers[] = {"Type", "loss", "target",
                "Number of trades", "Number of winners", "Percent of winners",
                "Total winning", "Total loss", "Average profit",
                "Number of long trades", "Number of long winner", "Percent of long winners",
                "Long winning", "Long loss", "Average profit of long",
                "Number of short trades", "Number of short winners", "Percent of short winners",
                "Short winning", "Short loss", "Average profit of short",
                "Average holding period", "Daily Average Profit"
        };
        File file = new File("outcome.csv");
        try {
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter);


            writer.writeNext(headers);
            int length = list.size();
            Stats stats;
            for (int i = 0; i < length; i++) {
                stats = list.get(i).stats;
                writer.writeNext(new String[]{
                        list.get(i).type, String.valueOf(list.get(i).loss), String.valueOf(list.get(i).target),
                        String.valueOf(stats.numberOfTrades), String.valueOf(stats.numberWinners), String.valueOf(stats.percentOfWinners),
                        String.valueOf(stats.totalWinnings), String.valueOf(stats.totalLoss), String.valueOf(stats.avgProfit),
                        String.valueOf(stats.numberLong), String.valueOf(stats.longWinners), String.valueOf(stats.percentOfLongWinners),
                        String.valueOf(stats.totalLongWinnings), String.valueOf(stats.totalLongLoss), String.valueOf(stats.avgLongProf),
                        String.valueOf(stats.numberShort), String.valueOf(stats.shortWinners), String.valueOf(stats.percentOfShortWinners),
                        String.valueOf(stats.totalShortWinnings), String.valueOf(stats.totalShortLoss), String.valueOf(stats.avgShortProf),
                        String.valueOf(stats.avgHoldingPeriod), String.valueOf(list.get(i).dailyProfit)
                });
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}