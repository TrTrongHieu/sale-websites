<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 8/26/2020
  Time: 5:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp" %>
<c:url var="APIurl" value="/api-admin-product"/>

<html>
<head>

</head>
<body>

<div class="panel-body">
    <form id="frmAdd">
        <div class="form-group">
            <label for="txtName">Name product</label>
            <input type="text" class="form-control" id="txtName" name="name" autofocus>
        </div>
        <div class="form-group">
            <label for="sel1">Select category:</label>
            <select class="form-control" id="sel1" name="categoryId">
                <c:forEach items="${listCategory}" var="item">
                    <option value="${item.id}">${item.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>Short description:</label>
            <input type="text" class="form-control" id="shortSummary" name="shortSummary">
        </div>

        <div class="form-group">
            <label for="content">Content:</label>
            <textarea class="form-control" rows="5" id="content"></textarea>
        </div>
        <div class="form-group">
            <label>Price:</label>
            <input type="number" class="form-control" id="price" name="price">
        </div>
        <div class="form-group">
            <label>Manufacturer:</label>
            <input type="text" class="form-control" id="manufacturer" name="manufacturer">
        </div>
        <div class="custom-file mb-3">
            <input type="file" class="custom-file-input" id="image" name="image">
            <label class="custom-file-label" for="image">Choose image</label>
        </div>
        <a href="<c:url value='/admin-product'/>" class="btn btn-success" role="button">
            <<
        </a>
        <button class="btn btn-primary" id="btnAdd">
            Save
        </button>

    </form>
</div>
</div>

<script>
    $('#btnAdd').click(function (e) {
        e.preventDefault();
        var data = {};
        var formData = $('#frmAdd').serializeArray();
        $.each(formData, function (i, v) {
            data["" + v.name + ""] = v.value;
            console.log(data);
        })

        addProduct(data);
    })

    function addProduct(data) {
        $.ajax({
            url: '${APIurl}',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            dataType: 'json'
        })
    }
</script>

</body>
</html>
