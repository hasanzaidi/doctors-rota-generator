# doctors-rota-generator
Command line application for generating a rota for doctors

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

## Parts
The application is split into two parts:
- Generate a rota over an initial 10 week period
- Given a doctors asks for leave for a particular period of days, suggest swaps if not possible to accomodate

## Shifts
There are three shifts:
* "Normal" - 09:00 - 17:00 (only on weekdays)
* "Long day" - 08:30 - 21:00
* "Night" - 20:30 - 09:00 (+1)

## Rules for rota generation
1. No doctor can work more than 47 hours per week on average. But they can work more than 47 hours in a single week.
1. No doctor can work more than 7 days in a row
1. After a night shift, a doctor must wait a minimum 13 hours before doing another shift.
1. Over a 10 week/10 doctor period they must do:
   * 4 long days which fall on a weekday (i.e. Monday-Thursday)
   * 4 nights on Monday-Thursday where the 4 nights must be consecutive
   * 1 long day weekend (i.e. a long day on Friday, Saturday and Sunday on same weekend)
   * 1 night weekend (i.e. a night on Friday, Saturday and Sunday on same weekend)
1. Only one doctor can work on an anti-social shift (i.e. long day or Night, either weekend/weekday)

## Rules for swaps
1. Cannot have more than 3-way swap (to ease complexity on doctors to stick to it)
1. When doing a long day/night on a weekend, can split over multiple weekends (to make swaps easier)

## Future features
1. Add concept of ward so that cannot have < 2 on a ward
1. Add ability to configure number of hours that each of the 10 doctors can work
1. Add ability to configure other values such as max hours, max days in a row
