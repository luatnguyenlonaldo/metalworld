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
        <h2 class="heading-title">Chi tiết sản phẩm</h2>

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

        <div id="popup1" class="overlay">
            <div class="popup">
                <h2>Đóng góp</h2>
                <a class="close" href="#">&times;</a>
                <hr/>
                <div class="content">
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>Email:</td>
                                <td><input style="width: 300px;" type="text" id="email" placeholder="Email của bạn là..."/></td>
                            </tr>
                            <tr>
                                <td>
                                    <label class="form-label" for="selectSkillLevel">Cấp độ kỹ năng của bạn</label>
                                </td>
                                <td>
                                    <select class="form-select" id="skillLevelContribution" 
                                            onchange="handleChangeLevel(this)">
                                        <option value="1">Mới bắt đầu</option>
                                        <option value="2">Tàm tạm</option>
                                        <option value="3">Quen thuộc</option>
                                        <option value="4">Thành thạo</option>
                                        <option value="5">Chuyên gia</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td>Thời gian hoàn thành: </td>
                                <td>
                                    <input style="width: 300px;" type="number" class="form-input text-right" min="0" step="0.1"
                                           id="completionTime" onchange="handleTimePickerChangeTotalHours()"
                                           value="0"/>
                                </td>
                                <td>tiếng</td>
                            </tr>
                        </tbody>
                    </table>
                    <hr/>
                    <button class="btn btn-primary" style="float: right; width: 100px;" onclick="contributionInfor()">Gửi</button>
                </div>
            </div>
        </div>
        <div id="snackbar">Thanks for your support!!!</div>
    </body>
</html>
