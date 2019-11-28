
public class Stats {
    public int numberOfTrades, numberLong, numberShort, numberWinners, numberLosers;
    public int longWinners, longLosers, shortWinners, shortLosers;
    public int numberDays, numberLongDays, numberShortDays;
    double totalWinnings, totalLoss, totalLongWinnings, totalLongLoss, totalShortWinnings;
    double totalShortLoss;

    double percentOfWinners, avgProfit, percentOfLongWinners, avgLongProf, percentOfShortWinners, avgShortProf, avgHoldingPeriod;

    public Stats() {
        numberOfTrades= numberLong= numberShort= numberWinners= numberLosers = 0;
        longWinners= longLosers= shortWinners= shortLosers = 0;
        numberDays= numberLongDays= numberShortDays=0;
        totalWinnings= totalLoss= totalLongWinnings= totalLongLoss= totalShortWinnings = 0.0;
        totalShortLoss = 0.0;

    }

    //TODO
    public String toString() {
        String s = "{";
        //display  numberOfTrades PercentWinners(numberWinners/numberOfTrades*100) AverageProfit((totalWinnings+ totalLoss)/numberOfTrades)
        //numberLong, PercentLongwinners, averageProfitWinners, numberShort, percent Shortwinners, averageProfitShort, averageHoldingPeriod(numberDays/numberOfTrades)
        //numbers separated by comma.


        /*if(numberOfTrades == 0){
            percentOfWinners = avgProfit = avgHoldingPeriod = 0;
        } else{
            percentOfWinners = (double) this.numberWinners / (double)this.numberOfTrades * 100;
            avgProfit = (this.totalWinnings + this.totalLoss) / (double)this.numberOfTrades;
            avgHoldingPeriod = (double)this.numberDays / (double)this.numberOfTrades;
        }

        if(numberLong == 0){
            percentOfLongWinners = avgLongProf = 0;
        } else {
            percentOfLongWinners = (double)this.longWinners / (double)this.numberLong * 100;
            avgLongProf = (this.totalLongWinnings + this.totalLongLoss) / (double)this.numberLong;
        }

        if(numberShort == 0){
            percentOfShortWinners = avgShortProf = 0;
        } else {
            percentOfShortWinners = (double)this.shortWinners / (double)this.numberShort * 100;
            avgShortProf = (this.totalShortWinnings + this.totalShortLoss) / (double)this.numberShort;
        }*/

        s += "Number of trades: " + this.numberOfTrades + ",\n";
        s += "Number of winners: " + this.numberWinners + ",\n";
        s += "Percent of winners: " + percentOfWinners + "%,\n";
        s += "Average profit: " + avgProfit +",\n";
        s += "total winning: " + (this.totalLongWinnings+this.totalShortWinnings) + ",\n";
        s += "total loss: " + (this.totalLongLoss + this.totalShortLoss) + ", \n";
        s += "Number of long trades: " + this.numberLong + ",\n";
        s += "Number of long winner: " + this.longWinners + ",\n";
        s += "Percent of long winners: " + percentOfLongWinners +"%,\n";
        s += "Total Long winning: " + this.totalLongWinnings + ", \n";
        s += "Total Long loss: " + this.totalLongLoss + ",\n";
        s += "Average profit of long trades: " + avgLongProf + ",\n";
        s += "Number of short trades: " + this.numberShort + ",\n";
        s += "Number of short winner: " + this.shortWinners + ",\n";
        s += "Percent of short winners: " + percentOfShortWinners + "%,\n";
        s += "Total short winning: " + this.totalShortWinnings + ",\n";
        s += "Total short loss: " + this.totalShortLoss + ",\n";
        s += "Average profit of short trades: " + avgShortProf + ",\n";
        s += "Average holding period: " + avgHoldingPeriod;
        return s;
    }


}