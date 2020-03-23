<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : main-model.xsl
    Created on : March 23, 2020, 1:28 AM
    Author     : Lonaldo
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="yes"/>

    <xsl:template match="main-model">
        <div class="column col-6 col-xs-12">
            <div class="card">
                <div class="card-image">
                    <img src="{image-src}" class="img-responsive" alt="{name}" title="{name}" width="100%"/>
                </div>
            </div>
        </div>
        <div class="column col-6 col-xs-12">
            <div class="card no-border">
                <div class="card-header">
                    <div class="card-title h3" id="model-name">
                        <xsl:value-of select="name"/>
                    </div>
                    <div class="card-body">
                        <table class="table">
                            <tbody>
                                <tr>
                                    <td>Số tờ</td>
                                    <td>
                                        <xsl:value-of select="num-of-sheets"/>
                                    </td>
                                </tr>
                                
                                <xsl:if test="string-length(num-of-parts) > 0">
                                    <tr>
                                        <td>Số chi tiết</td>
                                        <td>
                                            <xsl:value-of select="num-of-parts"/>
                                        </td>
                                    </tr>
                                </xsl:if>
                                
                                <tr>
                                    <td>Độ khó</td>
                                    <td>
                                        <xsl:value-of select="difficulty"/>
                                    </td>
                                </tr>
                                
                                <xsl:if test="string-length(format) > 0">
                                    <tr>
                                        <td>Định dạng</td>
                                        <td>
                                            <xsl:value-of select="format"/>
                                        </td>
                                    </tr>
                                </xsl:if>
                                
                                <tr>
                                    <td>Hướng dẫn</td>
                                    <td>
                                        <xsl:if test="has-instruction = 'true'">Có</xsl:if>
                                        <xsl:if test="string-length(has-instruction) = 0">Không</xsl:if>
                                        <xsl:if test="has-instruction = 'false'">Không</xsl:if>
                                    </td>
                                </tr>
                                
                                <xsl:if test="string-length(estimate-time) > 0">
                                    <tr>
                                        <td>Thời gian</td>
                                        <td>
                                            <xsl:if test="estimate-time &lt; 1">
                                                <xsl:value-of select="format-number(estimate-time * 60, '###,###')"/>
                                                phút
                                            </xsl:if>
                                            <xsl:if test="estimate-time >= 1">
                                                <xsl:value-of select="format-number(estimate-time, '###,###')"/> 
                                                giờ
                                            </xsl:if>
                                        </td>
                                    </tr>
                                </xsl:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="card-footer">
                        <a href="{link}" target="_blank" class="btn btn-lg btn-primary">Tải xuống</a>
                    </div>
                </div>
            </div>
        </div>
    </xsl:template>

</xsl:stylesheet>
