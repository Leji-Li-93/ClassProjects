import java.util.*;
import java.io.*;
public class symbolTester {
    //member variables
    private String mPath, mSymbol;
    private double mLoss, mTarget; // those are the rate
    private double stoploss, target;
    private tradeArray mTrades;
    private barArray mData;
    private static double FACTOR = 0.5;
    private int xDay = 10;

    public symbolTester(String symbol, String path, double loss, double target) {
        mPath = path; //first
        mSymbol = symbol;//first BAC
        mLoss = loss; //first 0.01
        mTarget = target;//first = 0.01
        mTrades = new tradeArray(200, 100);
        mData = new barArray(4000);
    }
    public tradeArray getTrades() {
        return mTrades;
    }

    /*private Direction pattern(int index) {
        Bar p = mData.elementAt(index);

        Bar q = mData.elementAt(index + 1);
        Bar r;
        // short trade case step 2
        if(isXDayHighest(index, xDay) && // start doing the short
                (p.High() > q.High()) && (p.Low() > q.Low())){
            // i+1 has lower low and lower high
            r = mData.elementAt(index + 2);
            // short trade case step 3
            if(r.Low() > q.Low() // i+2 has higher low
                    && r.High() > p.High() // close near or higher than high of i
                    && r.Close() >= r.High() - FACTOR * r.Range()

            ) //the close of i+2 is higher than its open
            {
                p = q;
                q = r;
                r = mData.elementAt(index + 3);
                // short trade case step 4
                if(r.Low() < p.Low() && r.High() < q.High() // i+3 has lower low and lower high
                        && r.Close() <= r.Low() + FACTOR *r.Range()) // i+3's close is below low of i+1
                {
                    // is the short trade case
                    stoploss = r.Close() * (1 + mLoss);
                    target = r.Close() * (1 - mTarget);
                    return Direction.SHORT;
                } else {
                    return Direction.NONE;
                }
            }
            // false case for short step 2
            else {
                return Direction.NONE;
            }
        }
        // long trade case step 2
        else if(isXDayLowest(index, xDay) && // start doing the long
                (p.High() < q.High()) && (p.Low() < q.Low())){
            // i+1 has higher high and higher low
            r = mData.elementAt(index + 2);
            // long trade step 3
            if(r.High() < q.High() //lower high and lower low
                    && r.Low() < p.Low() // close near or below low of i
                    && r.Close() <= r.Close() + FACTOR * r.Range() //
            ){
                // long trade step 4
                p = q;
                q = r;
                r = mData.elementAt(index + 3);
                if(r.High() > p.High() && r.Low() > q.Low() // i+3 has higher high and higher low
                        && r.Close() > p.High() // i+3's close is higher than i+1's high
                ){
                    // is long trade
                    stoploss = r.Close() * (1 - mLoss);
                    target = r.Close() * (1 + mTarget);
                    return Direction.LONG;
                } else {
                    return Direction.NONE;
                }
            } else {
                return Direction.NONE;
            }
        }
        else {
            return Direction.NONE;
        }
    }*/

    private Direction pattern(int ind){
        Bar p, q , r;
        p = mData.elementAt(ind);
        q = mData.elementAt(ind+1);
        if(isXDayHighest(ind, xDay)){
            if(p.High() > q.High() && p.Low() > q.Low()){
                r = mData.elementAt(ind+2);
                if(r.High() > p.High() && r.Low() > q.Low()
                        //&&(r.Close() >= p.High() - FACTOR * p.Range())
                ){
                    p = q;
                    q = r;
                    r = mData.elementAt(ind + 3);
                    if(r.Low() < p.Low() && r.High() < q.High()
                            //&&r.Close() < p.Low()
                    ){
                        stoploss = r.Close() * (1 + mLoss);
                        target = r.Close() * (1 - mTarget);
                        return Direction.SHORT;
                    } else {
                        return Direction.NONE;
                    }
                } else {
                    return Direction.NONE;
                }
            } else {
                return Direction.NONE;
            }
        }
        else if(isXDayLowest(ind, xDay)){
            if(p.Low() < q.Low() && p.High() < q.High()){
                r = mData.elementAt(ind+2);
                if(r.Low() < p.Low() && r.High() < q.High()
                        //&&(r.Close() <= p.Low() + FACTOR * p.Range())
                ){
                    p = q;
                    q = r;
                    r = mData.elementAt(ind+3);
                    if(r.High() > p.High() && r.Low() > q.Low()
                            //&& r.Close() > p.High()
                    ){
                        stoploss = r.Close() * (1 - mLoss);
                        target = r.Close() * (1 + mTarget);
                        return Direction.LONG;
                    } else {
                        return Direction.NONE;
                    }
                } else {
                    return Direction.NONE;
                }
            } else {
                return Direction.NONE;
            }
        }
        else {
            return Direction.NONE;
        }
    }

    public boolean isXDayHighest(int index, int x){
        boolean flag = true;
        double high = mData.elementAt(index).High();
        for (int i = 1; i < x; i++) {
            if(mData.elementAt(index - i).High() >= high){
                return false;
            }
        }
        return flag;
    }

    public boolean isXDayLowest(int index, int x){
        boolean flag = true;
        double low = mData.elementAt(index).Low();
        for (int i = 1; i < x; i++) {
            if(mData.elementAt(index - i).Low() <= low){
                return false;
            }
        }
        return flag;
    }

    public boolean test() {
        //load the data into mData
        if (!load()) {
            return false;
        }
        Direction patternDirection;
        //Now you have your data in mData mData.elementAt(0) is the oldest bar
        //mData.elementAt(0).close()
        for (int i = xDay - 1; i<mData.size()-3; i++){
            //go through mData bar by bar
            //if patter found (make a private method pattern(i) where i is the index of the
            //current bar
            if((patternDirection = pattern(i)) != Direction.NONE){
                //create a trade with  values
                Trade Tr = new Trade();
                // open the trade
                Tr.open(mSymbol,
                        mData.elementAt(i+3).getDate(),
                        mData.elementAt(i+3).Close(),
                        this.stoploss,
                        this.target,
                        patternDirection);
                //check the outcome of the trade, and close it
                // buy at the close of i+3
                outcome(Tr, i+3);
                //insert the trade into mTrades.
                mTrades.insert(Tr);
            }

        }
        return true;
    }
    public void outcome(Trade t, int ind){
        int length = mData.size();
        Bar bar;
        if(mLoss == 0.0 && mTarget == 0.0){
            t.close(mData.elementAt(ind+1).getDate(), mData.elementAt(ind+1).Close());
            return;
        }
        for (int i = ind+1; i < length; i++) {
            bar = mData.elementAt(i);
            // long
            // compare target with bar's high
            // compare stoploss with bar's low
            if(t.getDir() == Direction.LONG){
                // if both target and loss met, close the trade at entry price
                if(bar.High() > t.getTarget() && bar.Low() < t.getStopLoss()){
                    t.close(bar.getDate(), t.getEntryPrice());
                    return;
                }
                // if win -> bar high is higher than target
                if(bar.High() > t.getTarget()){
                    // if open price is higher than target
                    // close at open price
                    if(bar.Open() > t.getTarget()){
                        t.close(bar.getDate(), bar.Open());
                    } else { // otherwise close at target
                        t.close(bar.getDate(), t.getTarget());
                    }
                    return;
                }
                // if loss -> bar low is lower than loss
                if(bar.Low() < t.getStopLoss()){
                    // if open is lower than stop loss
                    // close at open
                    if(bar.Open() < t.getStopLoss()){
                        t.close(bar.getDate(), bar.Open());
                    } else { // otherwise close at stop loss
                        t.close(bar.getDate(), t.getStopLoss());
                    }
                    return;
                }
            }
            // short
            // compare target with bar's low
            // compare stoploss with bar's high
            else if(t.getDir() == Direction.SHORT){
                // if both target and stoploss met
                // close the trade at entry price
                if(bar.Low() < t.getTarget() && bar.High() > t.getStopLoss()){
                    t.close(bar.getDate(), t.getEntryPrice());
                    return;
                }
                // if win -> bar low is lower than target
                if(bar.Low() < t.getTarget()){
                    // close at the lower one between open price and target
                    if(bar.Open() < t.getTarget()){
                        t.close(bar.getDate(), bar.Open());
                    } else {
                        t.close(bar.getDate(), t.getTarget());
                    }
                    return;
                }
                // if loss -> bar high is higher than stop loss
                if(bar.High() > t.getStopLoss()){
                    // close at the higher one between open and stoploss
                    if(bar.Open() > t.getStopLoss()){
                        t.close(bar.getDate(), bar.Open());
                    } else {
                        t.close(bar.getDate(), t.getStopLoss());
                    }
                    return;
                }
            }
            // other cases, just in case
            else{
                System.out.println("Something wrong");
                return;
            }
        }
        // if comes to the end of the data but it doesn't reach the target or hit the stoploss
        // close at the close price of the last day
        if(t.isOn()){
            bar = mData.elementAt(mData.size() - 1);
            t.close(bar.getDate(), bar.Close());
            return;
        }
    }
    private boolean load() { //HW for the break
        //build a file with path name using mPath and mSymbol

        String fileName = mPath+mSymbol+"_Daily.csv";
        // first String fileName = "C:/ProfOmar286/Data/" + "BAC" +"_Daily.csv";
        //check if the file exists (use FILE object)
        File myfile = new File(fileName);
        if (!myfile.exists()) {
            System.out.println("file does not exist");
            return false;
        }
        try {
            FileReader fr = new FileReader(fileName);
            //open the file FileReader->BufferedReader
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();//discard this line
            while((line=br.readLine()) != null) {
                Bar b =new Bar(line);
                mData.insert(b);
            }
            br.close();
            return true;
        }catch(IOException e){
            System.out.println(e.toString());
            return false;
        }
    }
}