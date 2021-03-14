# Yeelight Stockprices

This is a simple Java program that changes the color of Xiaomi Yeelight bulbs based on a specific stock price.

price goes up -> green  
price goes down -> red  
price stays the same -> blue

The saturation and brightness is dependend on the last 15 seconds average price.


In order to control the Yeelights, the [YAPI API](https://github.com/florian-mollin/yapi) is being used.  
The `price()` function is copied from [rayning0](https://github.com/rayning0/Princeton-Algorithms-Java/blob/master/introcs/StockQuote.java) and was slightly modified.
