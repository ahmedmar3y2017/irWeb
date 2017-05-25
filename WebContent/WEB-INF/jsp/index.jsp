<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="utf-8">
<meta lang="ar">
<meta name="description"
	content="First distrubited , ranking Search Enginen">

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- Compatibility Meta IE -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- First Mobile Meta -->
<title>Search Enginen| Project</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css">
<link href="https://fonts.googleapis.com/css?family=Sansita"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/resources/images/logo2.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/style.css">

</head>


<body>
	<div class="background">
		<div class="container">

			<div class="all">
				<div class="logo">
					<img
						src="${pageContext.request.contextPath}/resources/images/logo2.png">
					<span> المنوفية</span><br>

				</div>


				<div class="main">

					<form class="forma"
						action="/IrWeb/done.html"
						method="get">
						<div class="input">
							<input type="text" name="Query" placeholder="ابحث هنا "
								autocomplete="on">
						</div>
				</div>

				<div class="btn">
					<button>ضربة حظ</button>
					<button>بحث</button>
				</div>

				</form>

			</div>
			<div class="Copyright">
				<span> © 2017 Copyright Sec1 IS </span>
			</div>
		</div>


	</div>

	<script
		src="${pageContext.request.contextPath}/resources/js/jquery-1.11.1.min.js"></script>

	<script src="${pageContext.request.contextPath}/resources/js/custom.js"></script>

</body>