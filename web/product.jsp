<%-- 
    Document   : product
    Created on : Mar 22, 2020, 5:48:41 PM
    Author     : Lonaldo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="img/icon.ico" />

        <link href="css/image-effect.css" rel="stylesheet" type="text/css"/>
        
        <link href="css/spectre.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/spectre-exp.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/spectre-icons.min.css" rel="stylesheet" type="text/css"/>
        
        <link href="css/home.css" rel="stylesheet" type="text/css"/>
        <script src="js/home.js" type="text/javascript"></script>
        <script src="js/product-detail.js" type="text/javascript"></script>
        <title>Product Detail</title>
    </head>
    <body>
        <div class="app-logo">
            <a href="http://localhost:8084/MetalWorld/">
                <img src="img/icon-metalworld.png" alt="MetalWorld" width="400" title="MetalWorld"/>
            </a>
        </div>
        <h2 class="heading-title">Chọn mẫu mô hình giấy của bạn</h2>

        <div id="div-loading" class="loading loading-lg" style="display: none">
        </div>

        <div id="model-not-found" class="empty" style="display: none">
            <div class="empty-icon">
                <i class="icon icon-4x icon-photo"></i>
            </div>
            <p class="empty-title h5">Chúng tôi hiện chưa cập nhật mẫu mô hình này!</p>
            <p class="empty-subtitle">Mô hình sẽ được thêm vào trong thời gian sớm nhất.</p>
            <div class="empty-action">
                <a href="." class="btn btn-primary">Tiếp tục khám phá mô hình</a>
            </div>
        </div>

        <div id="section-model-detail">
        </div>

        <div class="container" id="paginationContainer">
        </div>
    </body>
</html>
