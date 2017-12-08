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
    b_api_key = 'jHAFZpiffIBg6u5ypanMjRAo2MLmHZm2ECtTWfFwvqbfXEHdjeRspUos6u7qjr6G'
    b_api_secret = 'Cp8iLFxL15S11PbJcN37AHvwUgv8FT8tjiosjcyYhjTvT066AfbzQhn7Wr6lJJTw'
    #HitBTC
    h_api_key = '07cf7e0803a5dcb65b45df9074f271ea'
    h_api_secret = '317d84c2873787cd0a96cefab5826d59'

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

    print(h_client.get_orderbook("ETHBTC"))

#First requirement is we should be able to pass settings into the bot
if __name__ == "__main__":
    main(sys.argv[1:])
