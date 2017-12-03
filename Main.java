package com.company;

import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.*;

public class Main {
    static LinkedList<Stock> hitbtc = new LinkedList<Stock>();
    static LinkedList<Stock> binance = new LinkedList<Stock>();

    public static void main(String[] args) {

        getSymbols();
        compare();
    /*    while(true)
        {
            System.out.println("Trading...");
            update();
            sleep(5);
       }
       */
    }

    static public void sleep(int t) {
        try {
            Thread.sleep(t * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static public void getSymbols() {
        String ss[] = null;
        String ss2[] = null;
        String site = null;
        String symbol = null;
        String ask = null;
        String bid = null;
        URL url = null;


        try {
            site = "HITBTC";
            url = new URL("https://api.hitbtc.com/api/2/public/ticker");
            ss = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")).readLine().split("}");
            for (int i = 0; i < ss.length; i++) {
                if (ss[i].contains("USD"))
                    continue;
                ss[i] = ss[i].replace("[{", "").replace(",{", "").replace("\"", "").replace("]", "");
                ss2 = ss[i].split(",");
                for (int e = 0; e < ss2.length; e++) {
                    if (ss2[e].contains("ask"))
                        ask = ss2[e].split(":")[1];
                    if (ss2[e].contains("bid"))
                        bid = ss2[e].split(":")[1];
                    if (ss2[e].contains("symbol"))
                        symbol = ss2[e].split(":")[1];
                }
                if(ask.equalsIgnoreCase("null")||bid.equalsIgnoreCase("null")||symbol.equalsIgnoreCase("null"))
                    continue;
                hitbtc.add(new Stock(site, symbol, bid, ask));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            site = "BINANCE";
            url = new URL("https://www.binance.com/api/v1/ticker/allBookTickers");
            ss = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")).readLine().split("}");
            for (int i = 0; i < ss.length; i++) {
                ss[i] = ss[i].replace("[{", "").replace(",{", "").replace("\"", "").replace("]", "");
                ss2 = ss[i].split(",");
                for (int e = 0; e < ss2.length; e++) {
                    if (ss2[e].contains("askPrice"))
                        ask = ss2[e].split(":")[1];
                    if (ss2[e].contains("bidPrice"))
                        bid = ss2[e].split(":")[1];
                    if (ss2[e].contains("symbol"))
                        symbol = ss2[e].split(":")[1];
                }
                if(symbol.contains("BTG")||symbol.contains("BCC"))
                    continue;
                binance.add(new Stock(site, symbol, bid, ask));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void compare()
    {
        LinkedList<Stock> hitbtc2 = new LinkedList<Stock>();
        LinkedList<Stock> binance2 = new LinkedList<Stock>();

        for(int i=0;i<hitbtc.size();i++)
        {
            for(int e=0;e<binance.size();e++)
            {
                if(hitbtc.get(i).SameSymbol(binance.get(e)))
                {
                    hitbtc2.add(hitbtc.get(i));
                    binance2.add(binance.get(e));
                    binance.remove(e);
                    continue;
                }
            }
        }
        double hitask;
        double hitbid;
        double binask;
        double binbid;
        double A1;
        double A2;
        String sym;
        String buy;
        DecimalFormat df = new DecimalFormat("#.000000");
        for(int i=0;i<hitbtc2.size();i++)
        {
            sym=hitbtc2.get(i).Symbol;
            hitask=hitbtc2.get(i).Ask;
            hitbid=hitbtc2.get(i).Bid;
            binask=binance2.get(i).Ask;
            binbid=binance2.get(i).Bid;
            if(hitask>binask)
                buy="B->H";
            else
                buy="H->B";


            A1=(Math.max(hitask,binask)-Math.min(hitbid,binbid))/Math.min(hitbid,binbid)*100;
            A2=(Math.max(hitbid,binbid)-Math.min(hitask,binask))/Math.min(hitask,binask)*100;
            if(A2>0)
                System.out.println(buy+" "+sym+" "+df.format(A2));
        }

    }

    static public void update() {
        //  String Symbol="btgbtc";
        String Symbol = "ethbtc";
        String Ask = "X";
        String Bid = "X";
        String ss[] = null;
        URL url = null;
        try {
            url = new URL("https://api.hitbtc.com/api/2/public/ticker/" + Symbol);
            ss = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")).readLine().split("\"");
            for (int i = 0; i < ss.length; i++) {
                if (ss[i].contains("ask"))
                    Ask = ss[i + 2];
                if (ss[i].contains("bid"))
                    Bid = ss[i + 2];
            }
            Ask = String.format("%.5g", Double.valueOf(Ask) + (double) 0.000200);
            Bid = String.format("%.5g", Double.valueOf(Bid) - (double) 0.000200);
            String BuyTrade = "symbol=" + Symbol + "&side=buy&type=limit&timeInForce=GTC&quantity=0.001&price=" + Bid;
            String SellTrade = "symbol=" + Symbol + "&side=sell&type=limit&timeInForce=GTC&quantity=0.001&price=" + Ask;
            execute(SellTrade);
            execute(BuyTrade);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void execute(String Order) throws Exception {
        new ProcessBuilder(
                "C:\\curl.exe",
                "-X",
                "POST",
                "https://api.hitbtc.com/api/2/order",
                "-H",
                "accept: application/json",
                "-H",
                "authorization: Basic https://api.hitbtc.com/api/2/public/ticker",
                "-H",
                "Content-Type: application/x-www-form-urlencoded",
                "-d",
                Order).start();
    }
}

class Stock {
    String Site;
    String Symbol;
   // String Bid;
   // String Ask;
    double Bid;
    double Ask;

    Stock(String si, String sy, String b, String a) {
        Site = si;
        Symbol = sy;
    //    Bid=b;
    //    Ask=a;
        Bid = Double.valueOf(b);
        Ask = Double.valueOf(a);
    }

    public boolean SameSymbol(Stock s)
    {
        if(this.Symbol.equalsIgnoreCase(s.Symbol))
            return true;
        return false;
    }

    public String toString()
    {
        return Site+" "+Symbol+" "+Bid+" "+Ask;
    }
}
