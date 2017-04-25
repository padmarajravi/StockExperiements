Repository for personal stock market experiements

Work flow

Downloader

On starting up , system should check whether data is available for the last n days. If not, download last n days of data.

Data Format

Stock -- hasPrice --> (stock,open,high,low,close,date) -- onDate --> (Date)

TrendAnalyzer

Get the price of the last n days
Find the slope
If slope greater than slopeThreshold give out a buy