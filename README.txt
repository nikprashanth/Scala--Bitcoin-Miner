This folder has two folder 'client' and 'server'

STEPS TO EXECUTE SERVER:

1) Change the server IP in the application.config files accordingly.
2) Go to 'server' folder under which build.sbt file is available.
3) Open cmd and go to sbt prompt by giving command 'sbt' (eg: $path\DOS_Project1\Server>sbt)
4) To execute the server, give command "run-main BitcoinServer k" where k is no. of leading zeros (eg: $path\DOS_Project1\Server>run-main BitcoinServer 3)

STEPS TO EXECUTE CLIENT:

1) Change the client IP in the application.config files accordingly.
2) To start the client, Go to 'client' folder under which build.sbt file is available.
3) Open cmd and go to sbt prompt by giving command 'sbt' ($path\DOS_Project1\Client>sbt)
4) To execute the client, give command "run-main BitcoinClient ipaddress"($path\DOS_Project1\Client>run-main BitcoinClient 192.168.1.3)

GENERAL DESCRIPTION OF OUR MODEL:
Server has a Master-actor who assigns the work to the workers. The workers will run the sha-256 function on the server node to mine the bitcoins. Mined coins will
be aggregated at the Master-actor and then passed to the listener-actor to print the results to the console. 
If the client comes in the middle and request for the work from the server. Server's Master-actor assigns the work to Client's Master-actor. This Client's master does the 
job similar to what server does till aggregation. After Aggregation, all the coins will be passed to Server-Master, who passes the coins to it's Listener-actor to print
the bitcoins to the console. 

* CLient mines the coins but doesn't print the output to console, only passes to server, who does the job of printing these coins as well.

RESULTS:

The following result was tested on Windows operating systems, one is i5 processor of 4 cores and another one is i7 processor of 4 cores.

1) We observed that if work unit is 100000,it gave good performance.After every 100000 coins,worker reports to master.
We determined results for work units of 10000,50000,100000 and 200000. 

2) On running the code with k=4, We managed to mine more than 100 Bitcoins in a duration of 10 seconds. 
The result file (4zeros.txt) has been included in the DOS_Project1 zip.

3) For k=5, we managed to get 5 Bitcoins with a duration of 20 seconds.
The result file (5zeros.txt) has been included in the DOS_Project1 zip.

4) The coins with the most 0s we managed to find were 7: (refer 7zeros.txt for bitcoin)

5) We tested the project by deploying on 2 machines.

