# average-temperature
Average temperature - different models

Supported models (https://klimat.imgw.pl/pl/jednolite-serie-danych/):

```
Formulas used to calculate the official mean daily air temperature:

Synoptic stations since 1966:

Tsr = (T00 + T03 + T06 + T09 + T12 + T15 + T18 + T21) / 8

Synoptic stations in 1951-1965 and climatological stations in 1951-1995:

Tsr = (T06 + T12 + 2 * T18) / 4

Climatological stations since 1996:

Tsr = (T06 + T18 + Tmax + Tmin) / 4

Where

Txx – air temperature in measurement date xx (UTC),
Tmax – maximum daily air temperature,
Tmin – minimum daily air temperature.
```

In code are used first and third (average8 and average4).

All used average methods are:

* pl.maniecek.average.Average.average2
```
T2 = (Tmax + Tmin) / 2
```
* pl.maniecek.average.Average.average4
```
T4 = (T06 + T18 + Tmax + Tmin) / 4
```
* pl.maniecek.average.Average.average8
```
T8 = (T00 + T03 + T06 + T09 + T12 + T15 + T18 + T21) / 8
```
* pl.maniecek.average.Average.average24
  
T24 = $$\sum_{i=1}^n T_i$$

How to run:
First install sbt (https://www.scala-sbt.org/download/) and at least jdk 11.

Then run (quick - example.csv is input file with coordinates and timezone):

example.csv
```
latitude,longitude,city,timezone
53.091607,23.153443,Białystok,Europe/Warsaw
```

``
sbt ";project open-meteo; run openmeteo/src/pack/example.csv 2000 2024"
``
Or build pack version:

``
sbt ";project open-meteo; pack"
``

Next go to:

``
cd openmeteo/target/pack
``

and run:

``
bin/openmeteo example.csv 2000 2024
``

For windows use \ instead of /

For merging result use merge.py (change /tmp/output to your output dir):

``
python merge.py /tmp/output Basel
``

