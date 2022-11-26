## Description

Small Java 8 project to compare results of ```test="value = 0"``` in XSLT 1.0 and XSLT 2.0 for different values: 0, 0.00, -0, -0.00

## Results

```shell script
XSLT 1.0 result:
0 is 0
0.00 is 0
-0 is 0
-0.00 is 0

XSLT 2.0 result:
0 is 0
0.00 is 0
-0 is 0
Attention! -0.00 is NOT 0
```

## Run and build

```mvn test```
