<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : model-list.xsl
    Created on : March 21, 2020, 11:29 PM
    Author     : Lonaldo
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="yes"/>

    <xsl:param name="pageSize" select="60"/>
    <xsl:param name="checked" select="'false'"/>
    
    <xsl:template match="product-list">
        <xsl:if test="$checked = 'true'">
            <h5 class="suggestLabel">Có thể bạn quan tâm:</h5>
        </xsl:if>
        
        <xsl:for-each select="product[position() mod $pageSize = 1]">
            <div id="result-page-{position()}" class="columns hide">
                <xsl:apply-templates select="self::*|following-sibling::*[position() &lt; $pageSize]"/>
            </div>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="product">
        <div class="column col-3 col-md-4 col-sm-6 col-xs-12">
            <div class="card">
                <div class="card-image">
                    <div class="hover14">
                        <a href="/MetalWorld/product.jsp?id={id}" target="_blank">
                            <figure>
                                <img src="{image-src}" class="img-responsive" alt="{name}" title="{name}" width="100%"/>    
                            </figure>
                        </a>
                    </div>
                </div>
                <div class="card-header">
                    <div class="card-title h5">
                        <xsl:value-of select="name"/>
                    </div>
                </div>
                <!--<xsl:choose>-->
<!--                    <xsl:when test="(num-of-sheets=0) and (num-of-parts=0) and (difficulty=0)">
                        <div class="card-body">
                            <font color="red">
                                <h5>Hết hàng</h5>
                            </font>
                        </div>
                    </xsl:when>-->
                    <!--<xsl:otherwise>-->
                        <div class="card-body">
                            <div class="card-price">
                                <xsl:value-of select='format-number(price, "###,###")'/> VND
                            </div>
                        </div>
                        <div class="card-body">
                            Số tờ: 
                            <xsl:choose>
                                <xsl:when test="num-of-sheets > 0">
                                    <xsl:value-of select="num-of-sheets"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <i>Chưa cập nhật</i>
                                </xsl:otherwise>
                            </xsl:choose>
                            <br/>
                            Số chi tiết: 
                            <xsl:choose>
                                <xsl:when test="num-of-parts > 0">
                                    <xsl:value-of select="num-of-parts"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <i>Chưa cập nhật</i>
                                </xsl:otherwise>
                            </xsl:choose>
                            <br/>
                            Độ khó: 
                            <xsl:choose>
                                <xsl:when test="difficulty > 0">
                                    <xsl:value-of select="difficulty"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <i>Chưa cập nhật</i>
                                </xsl:otherwise>
                            </xsl:choose>
                            <br/>
                        </div>
                        <div class="card-footer">
                            <a href="/MetalWorld/product.jsp?id={id}" target="_blank">Xem chi tiết</a>
                        </div>
                    <!--</xsl:otherwise>-->
                <!--</xsl:choose>-->
            </div>
        </div>
    </xsl:template>
</xsl:stylesheet>
