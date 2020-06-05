# doctors-rota-generator
Command line application for generating a rota for doctors.

## Run Application
Run unit tests:
```
sbt clean test
```

Run application using:
```
sbt run
```

Package and run application as fat JAR:
```
sbt assembly
java -jar rota.jar 2020-06-01
```

Run Scala formatting on all files:
```
sbt scalafmtAll
```

## Parts
The application is split into two parts:
- Generate a rota over an initial 10 week period
- Given a doctors asks for leave for a particular period of days, suggest swaps if not possible to accomodate

## Shifts
There are three shifts:
* "Normal" - 09:00 - 17:00 (only on weekdays)
* "Long day" - 08:30 - 21:00
* "Night" - 20:30 - 09:00 (+1)
A weekday shift is defined as Monday-Thursday and a weekend shift is defined as Friday-Sunday.

## Rules for rota generation
1. No doctor can work more than 47 hours per week on average. But they can work more than 47 hours in a single week.
1. No doctor can work more than 7 days in a row
1. After a night shift, a doctor must wait a minimum 13 hours before doing another shift.
1. Over a 10 week/10 doctor period they must do:
   * 4 long days which fall on a weekday (i.e. Monday-Thursday).
   * 4 nights on a weekday. On the initial rota the 4 nights must be consecutive but can adjust when doing swaps.
   * 3 long days on a weekend. On the initial rota the 3 long days must be consecutive but can adjust when doing swaps.
   * 3 nights on a weekend. On the initial rota the 3 nights must be consecutive but can adjust when doing swaps.
1. Only one doctor can work on an anti-social shift (i.e. long day or Night, either weekend/weekday)

## Rules for swaps
1. Cannot have more than 3-way swap (to ease complexity on doctors to remember it)
1. When doing a long day/night on a weekend, can split over multiple weekends (to make swaps easier)

