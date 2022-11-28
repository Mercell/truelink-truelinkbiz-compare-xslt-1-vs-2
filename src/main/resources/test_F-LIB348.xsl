<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cac = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"
	xmlns:cbc = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"
	xmlns:doc = "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2">
		
	<xsl:output method="text" />
	
	<xsl:template match="doc:Invoice">
		<xsl:apply-templates select="cac:InvoiceLine"/>
	</xsl:template>
	
	<xsl:template match="cac:InvoiceLine">
		
		<xsl:variable name="quantity" select="cbc:InvoicedQuantity" />
		<xsl:variable name="priceAmount" select="cac:Price/cbc:PriceAmount" />
		<xsl:variable name="baseQuantity_1_12_3" select="concat(substring(cac:Price/cbc:BaseQuantity, 1, number(normalize-space(cac:Price/cbc:BaseQuantity) != '') * string-length(cac:Price/cbc:BaseQuantity)), substring(1, 1, number(not(normalize-space(cac:Price/cbc:BaseQuantity) != '')) * 1))" />
		<xsl:variable name="baseQuantity_1_13_1" select="cac:Price/cbc:BaseQuantity" />
		<xsl:variable name="lineExtensionAmount" select="format-number(cbc:LineExtensionAmount,'#.00')" />
		<xsl:variable name="calculatedTotalUnitCodeEqual_1_12_3" select="($priceAmount * $quantity) div $baseQuantity_1_12_3" />
		<xsl:variable name="calculatedTotalUnitCodeEqual_1_13_1" select="($priceAmount * $quantity) div $baseQuantity_1_13_1" />
		
		<xsl:value-of select="'calculatedTotalUnitCodeEqual_1_12_3 = '"/>
		<xsl:value-of select="$calculatedTotalUnitCodeEqual_1_12_3"/>
		
		<xsl:text>&#xa;</xsl:text>
		<xsl:value-of select="'calculatedTotalUnitCodeEqual_1_13_1 = '"/>
		<xsl:value-of select="$calculatedTotalUnitCodeEqual_1_13_1"/>
		<xsl:text>&#xa;</xsl:text>
		
	</xsl:template>
	
</xsl:stylesheet>
