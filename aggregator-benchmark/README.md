# Benchmark aggregators

## go-aggregator

```
ab -n 10000 -c 50 -k http://localhost:8080/aggregations
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 1000 requests
Completed 2000 requests
Completed 3000 requests
Completed 4000 requests
Completed 5000 requests
Completed 6000 requests
Completed 7000 requests
Completed 8000 requests
Completed 9000 requests
Completed 10000 requests
Finished 10000 requests


Server Software:
Server Hostname:        localhost
Server Port:            8080

Document Path:          /aggregations
Document Length:        91 bytes

Concurrency Level:      50
Time taken for tests:   325.219 seconds
Complete requests:      10000
Failed requests:        0
Keep-Alive requests:    10000
Total transferred:      2320000 bytes
HTML transferred:       910000 bytes
Requests per second:    30.75 [#/sec] (mean)
Time per request:       1626.096 [ms] (mean)
Time per request:       32.522 [ms] (mean, across all concurrent requests)
Transfer rate:          6.97 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       1
Processing:   194 1454 4840.0    562  277375
Waiting:      194 1454 4840.0    562  277374
Total:        194 1454 4840.0    562  277375

Percentage of the requests served within a certain time (ms)
  50%    562
  66%    728
  75%   1563
  80%   1682
  90%   2808
  95%   4271
  98%   7432
  99%  11443
 100%  277375 (longest request)
```

# node-aggregator

```
ab -n 10000 -c 50 -k http://localhost:3000/aggregations
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 1000 requests
Completed 2000 requests
Completed 3000 requests
Completed 4000 requests
Completed 5000 requests
Completed 6000 requests
Completed 7000 requests
Completed 8000 requests
Completed 9000 requests
Completed 10000 requests
Finished 10000 requests


Server Software:
Server Hostname:        localhost
Server Port:            3000

Document Path:          /aggregations
Document Length:        90 bytes

Concurrency Level:      50
Time taken for tests:   469.430 seconds
Complete requests:      10000
Failed requests:        0
Keep-Alive requests:    10000
Total transferred:      3020000 bytes
HTML transferred:       900000 bytes
Requests per second:    21.30 [#/sec] (mean)
Time per request:       2347.149 [ms] (mean)
Time per request:       46.943 [ms] (mean, across all concurrent requests)
Transfer rate:          6.28 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       1
Processing:   596 2343 902.6   2106   17218
Waiting:      596 2343 902.6   2106   17218
Total:        596 2343 902.6   2106   17218

Percentage of the requests served within a certain time (ms)
  50%   2106
  66%   2245
  75%   2757
  80%   2938
  90%   3187
  95%   3936
  98%   4921
  99%   5587
 100%  17218 (longest request)
```

## spring-web-aggregator

```
ab -n 10000 -c 50 -k http://localhost:8080/aggregations
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 1000 requests
Completed 2000 requests
Completed 3000 requests
Completed 4000 requests
Completed 5000 requests
Completed 6000 requests
Completed 7000 requests
Completed 8000 requests
Completed 9000 requests
Completed 10000 requests
Finished 10000 requests


Server Software:
Server Hostname:        localhost
Server Port:            8080

Document Path:          /aggregations
Document Length:        90 bytes

Concurrency Level:      50
Time taken for tests:   156.856 seconds
Complete requests:      10000
Failed requests:        19
   (Connect: 0, Receive: 0, Length: 19, Exceptions: 0)
Non-2xx responses:      19
Keep-Alive requests:    0
Total transferred:      1952266 bytes
HTML transferred:       902266 bytes
Requests per second:    63.75 [#/sec] (mean)
Time per request:       784.282 [ms] (mean)
Time per request:       15.686 [ms] (mean, across all concurrent requests)
Transfer rate:          12.15 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.2      0       2
Processing:   197  782 526.1    639   10980
Waiting:      197  782 526.1    638   10980
Total:        198  782 526.1    639   10980

Percentage of the requests served within a certain time (ms)
  50%    639
  66%    794
  75%    941
  80%   1002
  90%   1297
  95%   1609
  98%   2281
  99%   2703
 100%  10980 (longest request)
```

# spring-webflux-aggregator

```
 ab -n 10000 -c 50 -k http://localhost:8080/aggregations
This is ApacheBench, Version 2.3 <$Revision: 1807734 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking localhost (be patient)
Completed 1000 requests
Completed 2000 requests
Completed 3000 requests
Completed 4000 requests
Completed 5000 requests
Completed 6000 requests
Completed 7000 requests
Completed 8000 requests
Completed 9000 requests
Completed 10000 requests
Finished 10000 requests


Server Software:
Server Hostname:        localhost
Server Port:            8080

Document Path:          /aggregations
Document Length:        90 bytes

Concurrency Level:      50
Time taken for tests:   42.184 seconds
Complete requests:      10000
Failed requests:        0
Keep-Alive requests:    0
Total transferred:      1610000 bytes
HTML transferred:       900000 bytes
Requests per second:    237.05 [#/sec] (mean)
Time per request:       210.922 [ms] (mean)
Time per request:       4.218 [ms] (mean, across all concurrent requests)
Transfer rate:          37.27 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       1
Processing:   181  210 103.1    200    6208
Waiting:      181  210 103.1    200    6208
Total:        181  210 103.1    200    6208

Percentage of the requests served within a certain time (ms)
  50%    200
  66%    204
  75%    207
  80%    210
  90%    217
  95%    235
  98%    288
  99%    336
 100%   6208 (longest request)
```
