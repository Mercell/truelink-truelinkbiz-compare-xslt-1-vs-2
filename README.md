## Description

Small Java 8 project to analyse issues with OIOUBL Schematron release 1.13, which is planned to switch to XSLT 2.0 from XSLT 1.0

It incldues:

- compare results of ```test="value = 0"``` in XSLT 1.0 and XSLT 2.0 for different values: 0, 0.00, -0, -0.00
- some test cases of Invoice OIOUBL, where 1.12.3 on XSLT 1.0 and 1.13.1 on XSLT 2.0 provide different validation results.

## Results of ```test="value = 0"```

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

## Reasons for check of ```test="value = 0"```

In OIOUBL Schematron 1.13.0 a migration from XSLT 1.0 to XSLT 2.0 is done.

But as a result, some parts of old and tested checks started to work in a different way - and one of them is a comparison with 0.

At least one rule is affected by this - "[F-LIB381] Invalid VAT TaxAmount", where test condition looks like

> cac:TaxCategory/cbc:ID = 'StandardRated' and cbc:TaxableAmount > 0 and **cbc:TaxAmount = 0** and xs:decimal($CalculatedVat) >= 0.005

and applied on at least 3 levels - at TaxTotalCheck, TaxSubtotalCheck and TaxSubtotalCalculationHeader

## Schematrons comparison

The tool is extended with simple schematron versions comparison. 

The comparator goes through files in ```examples``` folder and validates it against two different version schematrons.

Examples are usually files which validation results are different to highlight the way how changes in schematron affect the industry.

## Results of different test cases

```shell script
----- Compare 1.12.3_xalan vs 1.13.1.eba0ace -----

Validate OIOUBL_Invoice_F-LIB336.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is not valid.
Errors:
[F-LIB336] PaymentMeansCode = 93, InstructionID must be a numeric value when PaymentID equals 71 or 75.

Checking OIOUBL-2.1 Invoice, 2022-12-21, Version 1.13.1.eba0ace
Document is valid.

Validate OIOUBL_Invoice_F-LIB381.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is not valid.
Errors:
[F-LIB381] Invalid VAT TaxAmount - When TaxCategory/ID are 'StandardRated' and TaxableAmount > 0 (600.00) TaxAmount can't be '(-0.00)' unless calculated vat '(150)' are less then 0.005
[F-INV127] The sum of TaxTotal/TaxSubtotal/TaxAmount elements must equal TaxExclusiveAmount

Checking OIOUBL-2.1 Invoice, 2022-12-21, Version 1.13.1.eba0ace
Document is valid.

Validate OIOUBL_Invoice_F-LIB382_persent_.00_should_fail_in_1.13.1.eba0ace.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is valid.

Checking OIOUBL-2.1 Invoice, 2022-12-21, Version 1.13.1.eba0ace
Document is valid. BUT SHOULD HAVE FAILED !!!

Validate OIOUBL_Invoice_F-LIB382_persent_0.00_should_fail_in_1.13.1.eba0ace.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is valid.

Checking OIOUBL-2.1 Invoice, 2022-12-21, Version 1.13.1.eba0ace
Document is not valid.
Errors:
[F-LIB382] When 'TaxCategor\ID' = 'StandardRated', 'TaxCategory\Percent' can not be '0.00'

Validate OIOUBL_Invoice_F-LIB382_persent_0.0_should_fail_in_1.13.1.eba0ace.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is valid.

Checking OIOUBL-2.1 Invoice, 2022-12-21, Version 1.13.1.eba0ace
Document is valid. BUT SHOULD HAVE FAILED !!!

Validate OIOUBL_Invoice_F-LIB382_persent_00_should_fail_in_1.13.1.eba0ace.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is valid.

Checking OIOUBL-2.1 Invoice, 2022-12-21, Version 1.13.1.eba0ace
Document is valid. BUT SHOULD HAVE FAILED !!!

Validate OIOUBL_Invoice_F-LIB382_persent_0_should_fail_in_1.13.1.eba0ace.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is valid.

Checking OIOUBL-2.1 Invoice, 2022-12-21, Version 1.13.1.eba0ace
Document is valid. BUT SHOULD HAVE FAILED !!!

Validate OIOUBL_Invoice_W-LIB222.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is not valid.
Errors:
[W-LIB222] The attribute languageID should be used when more than one Description element is present

Checking OIOUBL-2.1 Invoice, 2022-12-21, Version 1.13.1.eba0ace
Document is valid.

----- Compare 1.12.3_xalan vs 1.13.1.d5ee2f1 -----

Validate OIOUBL_Invoice_F-LIB312.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is valid.

Checking OIOUBL-2.1 Invoice, 2023-01-06, Version 1.13.1.d5ee2f1
Document is not valid.
Errors:
[F-LIB312] PaymentMeansCode = 50, InstructionID must be a numeric value when PaymentID equals 04 or 15.

Validate OIOUBL_Invoice_F-LIB382_persent_-0.00_should_fail_in_1.13.1.d5ee2f1.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is valid.

Checking OIOUBL-2.1 Invoice, 2023-01-06, Version 1.13.1.d5ee2f1
Document is valid. BUT SHOULD HAVE FAILED !!!

Validate OIOUBL_Invoice_F-LIB382_persent_-0_should_fail_in_1.13.1.d5ee2f1.xml

Checking OIOUBL-2.1 Invoice, 2022-05-19, Version 1.12.3.d5e8218
Document is valid.

Checking OIOUBL-2.1 Invoice, 2023-01-06, Version 1.13.1.d5ee2f1
Document is valid. BUT SHOULD HAVE FAILED !!!
```

## Run and build

```mvn test```
