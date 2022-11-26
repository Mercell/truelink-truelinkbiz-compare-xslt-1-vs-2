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

## Reasons for check

In OIOUBL Schematron 1.13.0 a migration from XSLT 1.0 to XSLT 2.0 is done.

But as a result, some parts of old and tested checks started to work in a different way - and one of them is a comparison with 0.

At least one rule is affected by this - "[F-LIB381] Invalid VAT TaxAmount", where test condition looks like

> cac:TaxCategory/cbc:ID = 'StandardRated' and cbc:TaxableAmount > 0 and **cbc:TaxAmount = 0** and xs:decimal($CalculatedVat) >= 0.005

and applied on at least 3 levels - at TaxTotalCheck, TaxSubtotalCheck and TaxSubtotalCalculationHeader

## Run and build

```mvn test```
