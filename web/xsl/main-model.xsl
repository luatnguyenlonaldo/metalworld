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
<!--                        <xsl:choose>
                            <xsl:when test="(num-of-sheets=0) and (num-of-parts=0) and (difficulty=0)">
                                <div class="card-body">
                                    <font color="red">
                                        <h5>Hết hàng</h5>
                                    </font>
                                </div>
                            </xsl:when>-->
                            <!--<xsl:otherwise>-->
                                <table class="table">
                                    <tbody>
                                        <tr>
                                            <td>Số tờ</td>
                                            <td>
                                                <xsl:choose>
                                                    <xsl:when test="num-of-sheets > 0">
                                                        <xsl:value-of select="num-of-sheets"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <i>Chưa cập nhật</i>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Số chi tiết</td>
                                            <td>
                                                <xsl:choose>
                                                    <xsl:when test="num-of-parts > 0">
                                                        <xsl:value-of select="num-of-parts"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <i>Chưa cập nhật</i>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Kích thước</td>
                                            <td>
                                                <xsl:choose>
                                                    <xsl:when test="string-length(size) > 0">
                                                        <xsl:value-of select="size"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <i>Chưa cập nhật</i>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Màu sắc</td>
                                            <td>
                                                <xsl:choose>
                                                    <xsl:when test="string-length(color) > 0">
                                                        <xsl:value-of select="color"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <i>Chưa cập nhật</i>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Độ khó</td>
                                            <td>
                                                <xsl:choose>
                                                    <xsl:when test="difficulty > 0">
                                                        <xsl:value-of select="difficulty"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <i>Chưa cập nhật</i>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Giá tiền</td>
                                            <td>
                                                <xsl:choose>
                                                    <xsl:when test="price > 0">
                                                        <xsl:value-of select='format-number(price, "###,###")'/> VND
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <i>Chưa cập nhật</i>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                                <div class="card-footer">
                                    <a href="{link}" target="_blank" class="btn btn-lg btn-primary">Tới nơi bán</a>
                                </div>
                            <!--</xsl:otherwise>-->
                        <!--</xsl:choose>-->
                    </div>
                    
                </div>
                <div class="contribute">
                    <font color="red">* Đóng góp: </font>
                    <a class="" href="#popup1">
                        <i>Bạn đã hoàn thành mẫu này mất bao lâu nhỉ?</i>
                    </a>
                </div>
            </div>
        </div>
        
    </xsl:template>

</xsl:stylesheet>
