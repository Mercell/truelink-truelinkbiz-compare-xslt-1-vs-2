<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text"/>
    <xsl:template match="data">
        <xsl:for-each select="test">
            <xsl:choose>
                <xsl:when test="string(number(value)) = 'NaN'"><xsl:value-of select="value"/> is not numeric</xsl:when>
                <xsl:otherwise>Attention! <xsl:value-of select="value"/> IS numeric</xsl:otherwise>
            </xsl:choose>
            <xsl:text>&#xa;</xsl:text>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
