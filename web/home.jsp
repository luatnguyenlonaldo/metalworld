<%-- 
    Document   : home
    Created on : Mar 20, 2020, 10:08:47 AM
    Author     : Lonaldo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Metal World | Thế giới nằm trong tầm tay</title>
        <link rel="shortcut icon" href="img/icon.ico" />
        <link href="css/spectre.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/spectre-exp.min.css" rel="stylesheet" type="text/css"/>
        <link href="css/spectre-icons.min.css" rel="stylesheet" type="text/css"/>

        <link href="css/home.css" rel="stylesheet" type="text/css"/>
        <script src="js/home.js" type="text/javascript"></script>
    </head>
    <body> 
       <div class="app-logo">
            <a href="http://localhost:8084/MetalWorld/">
                <img src="img/icon-metalworld.png" alt="MetalWorld" width="400" title="MetalWorld"/>
            </a>
        </div>

        <h2 class="heading-title">Khám phá các mô hình kim loại</h2>
        <div class="container" style="width: 70%">
            <div class="columns">
                <div class="column card-4" style="border-radius: 10px;">
                    
                    <form onsubmit="return false">
                        <div class="form-group">
                            <div>
                                <input type="text" id="modelName" class="form-input" style="float: left; width: 85%"; 
                                       placeholder="Nhập tên mô hình - Ví dụ: Thanos"/>
                                <button style="width: 15%" class="btn btn-primary" onclick="searchModels()">Tìm mô hình</button>
                            </div>
                        </div>
                    </form>
                    <br/>
                    <div class="separator"> hoặc </div>
                    <br/>

                    <form method="" onsubmit="return false">
                        <table class="table-model" border="0">
                            <tbody>
                                <tr>
                                    <td>
                                        <label class="form-labe text-label" for="selectSkillLevel" style="font-weight: bold">Kỹ năng</label>
                                    </td>
                                    <td>
                                        <label class="form-label text-label" for="selectDifficulty" style="font-weight: bold">Độ khó</label>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="form-group">
                                            <select class="form-select" id="selectSkillLevel" 
                                                    onchange="handleChangeLevel(this)">
                                                <option value="1">Mới bắt đầu</option>
                                                <option value="2">Tàm tạm</option>
                                                <option value="3">Quen thuộc</option>
                                                <option value="4">Thành thạo</option>
                                                <option value="5">Chuyên gia</option>
                                            </select>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="form-group">
                                            <select class="form-select" id="selectDifficulty" 
                                                    onchange="handleChangeLevel(this)">
                                                <option value="1">Cực dễ</option>
                                                <option value="2">Dễ</option>
                                                <option value="3">Trung bình</option>
                                                <option value="4">Khó</option>
                                                <option value="5">Cực khó</option>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>

                        <div class="form-group">
                            <label class="form-label text-label" style="font-weight: bold">Quỹ thời gian đầu tư cho mô hình</label>
                            <div class="input-group">
                                <input type="number" class="form-input text-right" min="0" step="0.1"
                                       id="makeTimeTotalHours" onchange="handleTimePickerChangeTotalHours()"
                                       value="0"/>
                                <span class="input-group-addon addon-custom">tiếng</span>
                            </div>
                        </div>
                        <div class="form-group text-center">
                            <button class="btn btn-primary" onclick="suggestModels()">Gợi ý mô hình</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div id="section-search-result">
            <div id="div-loading" class="loading loading-lg" style="display: none"></div>
            <div id="search-result" style="display: none">

            </div>
        </div>
        <div class="container" id="paginationContainer">
        </div>
    </body>
</html>
