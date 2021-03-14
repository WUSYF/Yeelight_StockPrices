import com.mollin.yapi.YeelightDevice;
import com.mollin.yapi.enumeration.YeelightEffect;
import com.mollin.yapi.exception.YeelightResultErrorException;
import com.mollin.yapi.exception.YeelightSocketException;

/**
 * Created by WUSYF on 08.03.2021
 */
public class Main {
    public static void main(String[] args) {
        YeelightDevice[] lamps = new YeelightDevice[5];
        String[] ips = {"192.168.1.22", "192.168.1.29", "192.168.1.24", "192.168.1.28", "192.168.1.26", "192.168.1.19"};
        Double[] priceHistory = new Double[15];
        String symbol = "GME";

        priceHistory[0] = price(symbol);
        for (int i = 0; i < priceHistory.length; i++) { //since I need/want the array to be filled with values, in order for the average/compare functions to work (correctly?) im just filling it with the first value I get from yahoo. This might be bad but I couldn't think of anything better.
            priceHistory[i] = priceHistory[0];
        }

        //INITIALISIERUNG
        System.out.println("initialisierung:");
        try {
            for (int i = 0; i < ips.length - 1; i++) {
                lamps[i] = new YeelightDevice(ips[i], 55443, YeelightEffect.SMOOTH, 1000);
                lamps[i].setPower(false);
                lamps[i].setPower(true);
            }
            System.out.println("STONK MEASURING CAN START NOW ");
            System.out.println("          __\n" +
                    "     w  c(..)o   (\n" +
                    "      \\__(-)    __)\n" +
                    "          /\\   (\n" +
                    "         /(_)___)\n" +
                    "         w /|\n" +
                    "          | \\\n" +
                    "          m  m");
        } catch (YeelightSocketException e) {
            System.out.println("Check the IPs and the bulbs connection!");
            e.printStackTrace();
        } catch (YeelightResultErrorException e) {
            e.printStackTrace();
        }

        //FARBEN AENDERN
        try {
            do {
                priceHistory[0] = price(symbol);

                if(priceHistory[0] > priceHistory[1]){
                    System.out.println(priceHistory[0] + " | STONKS!");
                    int intensity = intensity(priceDiff(priceHistory)/priceHistory[0]);
                    for (YeelightDevice l:lamps) {
                        l.setRGB(intensity,255,intensity);
                        l.setBrightness(100-(intensity/2));
                    }
                }else if(priceHistory[0] < priceHistory[1]) {
                    System.out.println(priceHistory[0] + " | not stonks :(");
                    int intensity = intensity(priceDiff(priceHistory)/priceHistory[0]);
                    for (YeelightDevice l:lamps) {
                        l.setRGB(255,intensity,intensity);
                        l.setBrightness(100-(intensity/2));
                    }
                }else {
                    System.out.println(priceHistory[0] + " | neutral stonks");
                    int intensity = intensity(priceDiff(priceHistory)/priceHistory[0]);
                    for (YeelightDevice l:lamps) {
                        l.setRGB(intensity,intensity,255);
                        l.setBrightness(100-(intensity/2));
                    }
                }

                for (int i = priceHistory.length - 1; i > 0; i--) {
                    priceHistory[i] = priceHistory[i-1];
                }
                Thread.sleep(1001);
            } while (true);

        } catch (YeelightResultErrorException e) {
            e.printStackTrace();
        } catch (YeelightSocketException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static double price(String symbol) {
        In page = new In("https://finance.yahoo.com/q?s=" + symbol);
        String input = page.readAll();
        int from = input.indexOf("Trsdu(0.3s) Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(b)", 0); //this should be done with regex..
        String price = input.substring(from + 72, from + 78);
        return Double.parseDouble(price);
    }


    public static Double priceDiff(Double[] p) { //p[0] is the current price
        Double averagePrice = 0.0;
        for (int i = 1; i < p.length; i++) {
            averagePrice += p[i];
        }
        averagePrice = averagePrice/p.length;
        return p[0] - averagePrice;
    }

    //returns value to mix into RGB, to make green/red less/more saturated, based on how much the price changed (compared to the average of the last 15 seconds)
    //I haven't tested this yet as it is weekend and the market is closed.
    public static int intensity(Double pcp){ //price change percentage
        if(pcp > 0.02){ //basically if the price change is above 2%, it will be 100% green. I'm sure there is a better way to do this
            pcp = 0.02;
        }
        return (int)Math.round(200 - (200*(pcp/0.02))); //a small price change will result in 200 being returned, which will create a very light green
    }
}
