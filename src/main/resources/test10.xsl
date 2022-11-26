<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text"/>
    <xsl:template match="data">
        <xsl:for-each select="test">
            <xsl:choose>
                <xsl:when test="value = 0"><xsl:value-of select="value"/> is 0</xsl:when>
                <xsl:otherwise>Attention! <xsl:value-of select="value"/> is NOT 0</xsl:otherwise>
            </xsl:choose>
            <xsl:text>&#xa;</xsl:text>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
