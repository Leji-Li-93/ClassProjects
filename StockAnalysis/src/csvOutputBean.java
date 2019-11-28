public class csvOutputBean {
    String type;
    double loss, target;
    Stats stats;
    double dailyProfit;

    public csvOutputBean(String type, double loss, double target, Stats stats){
        this.type = type;
        this.loss = loss;
        this.target = target;
        this.stats = stats;
        this.dailyProfit = this.stats.avgProfit / this.stats.avgHoldingPeriod;
    }
}
