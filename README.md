<h1>Give Me The Odds !</h1>

<h2>Introduction</h2>

This repository contains my application for GiskardAI's technical test.  
You can find the subject of this technical test at https://github.com/lioncowlionant/developer-test.  
You may find the back-end of the project with the server under GiveMeTheOdds.  
And the front end under giveMeTheOdds-frontend.  

<h2>Run the program</h2>

<p>There are multiple ways to run this program :</p>

<h3>1. Server + frontEnd version</h3>

To run the server, please first clone the repository.   
Using your favorite IDE, you'll find the class named "Main" in the project under **src/fr/msauce/giveMeTheOdds**.   
Run the class to launch the server.   

Please note that this project is compiled using JDK16.  
You might encounter issues if you happen to use an older version.  

<p>Once you've launched the server, launch a terminal from the root of the project and do : </p>

~~~
cd giveMeTheOdds-frontend
ng serve -o
~~~

Please note that you will need Angular CLI installed to run the server.  
You may get it by following the tutorial at this URL : https://angular.io/cli  

<p>You can now access the front-end at http://localhost:4200 (Note that -o in the last command should open that window for you)</p>

<h3>2. Command Line version</h3>

<p>To use the Command Line version of the program, please first clone the repository.<br>  
Using a terminal from the project's root, do :</p>

~~~
cd GiveMeTheOdds/target
java -jar ./GiveMeTheOdds.jar (or .\GiveMeTheOdds.jar depending on your OS) [file1] [file2]
~~~

With **file1** being the path to your __millennium-falcon.json.txt__ file.    
And **file2** the path to your **empire.json.txt** file.


