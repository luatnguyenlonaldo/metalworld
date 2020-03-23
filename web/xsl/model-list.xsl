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
    <xsl:param name="isRelatedModels" select="'false'"/>
    
    <xsl:template match="product-list">
        <xsl:if test="$isRelatedModels = 'true'">
            <h5 class="text-italic">Mô hình tương tự:</h5>
        </xsl:if>
        
        <xsl:for-each select="product[position() mod $pageSize = 1]">
            <div id="result-page-{position()}" class="columns hide">
                <xsl:apply-templates select="self::*|following-sibling::*[position() &lt; $pageSize]"/>
            </div>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="product">
        <div class="column col-2 col-md-4 col-sm-6 col-xs-12">
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
                <div class="card-body">
                    Số tờ: <xsl:value-of select="num-of-sheets"/>
                    <br/>
                    <xsl:if test="string-length(num-of-parts) > 0">
                        Số chi tiết: <xsl:value-of select="num-of-parts"/>
                        <br/>
                    </xsl:if>
                    Độ khó: <xsl:value-of select="difficulty"/>
                    <br/>
                    <xsl:if test="string-length(estimate-time) > 0">
                        Thời gian: 
                        <xsl:if test="estimate-time &lt; 1">
                            <xsl:value-of select="format-number(estimate-time * 60, '###,###')"/>
                            phút
                        </xsl:if>
                        <xsl:if test="estimate-time >= 1">
                            <xsl:value-of select="format-number(estimate-time, '###,###')"/> 
                            giờ
                        </xsl:if>
                        <br/>
                    </xsl:if>
                </div>
                <div class="card-footer">
                    <a href="/MetalWorld/product.jsp?id={id}" target="_blank">Xem chi tiết</a>
                </div>
            </div>
        </div>
    </xsl:template>
</xsl:stylesheet>
