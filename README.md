# istumapaikat-mn
Micronaut-based implementation for seat randomizing hack. Same as istumapaikat-web.

## Installation
Clone this repository. Run `./gradlew bootRun` or `gradlew.bat bootRun` or install Gradle and Grails with http://sdkman.io .

## Usage
Call http://localhost:8080/action/initialize to read the CSV files. Better get those from somewhere as well...

Call http://localhost:8080/action/randomize for a good t... for a distribution of seatconsumers to rooms

Call http://localhost:8080/action/draftpicks for a randomized seatconsumer list

## Contents
Micronaut application (1.0.0.M1) with H2 in memory database,GORM data services and CSV readers. See `controllers`- and `services`-packages for core functionalities.

