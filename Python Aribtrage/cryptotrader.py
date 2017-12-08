#Need to make a bot that makes trades for us
#Just a loop that repeats every so many seconds, time loops for service like scripts

import time
import sys
#Binance client was imported as a package via pip
from binance.client import Client
from HitBTC_Client import H_Client
from binance.enums import *

def main(argv):
    period = 10;

    ########### API KEYS ###########
    #Binance
    b_api_key = 'newb'
    b_api_secret = 'noob'
    #HitBTC
    h_api_key = 'nub'
    h_api_secret = '...'

    #### Client Declarations ####

    b_client = Client(b_api_key, b_api_secret)
    h_client = H_Client("https://api.hitbtc.com", h_api_key, h_api_secret)

#############
#Get Market Depth
    depth = b_client.get_order_book(symbol='BNBBTC')
#Get Recent Trades
    trades_recent = b_client.get_recent_trades(symbol='BNBBTC')
#Get Historical Trades
    trades_hist = b_client.get_historical_trades(symbol='BNBBTC')
#Get Aggregate trades
    trades_agg = b_client.get_aggregate_trades(symbol='BNBBTC')
#Get Kline/Candlesticks
    candles = b_client.get_klines(symbol='BNBBTC', interval=KLINE_INTERVAL_30MINUTE)
#Get 24hr Ticker
    tickers_24 = b_client.get_ticker()
#Get All Prices: Get last price for all markets
    prices = b_client.get_all_tickers()
#Get Orderbook Tickers: Get first bid and ask entry in the order book for all markets
    tickers_order = b_client.get_orderbook_tickers()
############

    # print(h_client.get_orderbook("ETHBTC"))
    ethbtc_orderbook = h_client.get_orderbook("ETHBTC")

#Check to see which coins can be traded and withdrawn
    h_cryptolist = []
    h_currencies = h_client.currencies()
    for currency in h_currencies:
        if currency['id'] == 'BTC':
            print(currency)


    minimum_ask = 0
    minimum_bid = 0

    for order, price in ethbtc_orderbook.items():
        print(order,":",price)
        print(len(price))
        # for i in price.length():
        if order == 'ask':
            pass
            # print("minimum ask price is: ")
        if order == 'bid':
            pass

#First requirement is we should be able to pass settings into the bot
if __name__ == "__main__":
    main(sys.argv[1:])
