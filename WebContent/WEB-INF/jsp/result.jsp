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
<title>Result Page</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css">
<link href="https://fonts.googleapis.com/css?family=Sansita"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">

<link rel="shortcut icon"
	href="${pageContext.request.contextPath}/resources/images/logo2.png">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/result.css">

</head>



<body>
	<div class="clear background">
		<div class="container">

			<div class="header">
				<div class="logo">
					<img
						src="${pageContext.request.contextPath}/resources/images/logo2.png">
					<input type="text" name="Query" placeholder="" autocomplete="on">
					<img
						src="${pageContext.request.contextPath}/resources/images/search-icon-large.png">
				</div>

			</div>

		</div>
	</div>
	<section class="docs">
		<div class="container">
			<div class="info">
				<span> حوالى <span>100</span> من النتائج عدد الثوانى <span>10
				</span>
				</span>
			</div>



			<div class="DOCS">


				<%-- ${element} --%>
				<c:forEach items="${result}" var="element">

					<!-- href = path of text  -->
					<div class="DOC">
						<a
							href="${pageContext.request.contextPath}/resources/files/${element}">${element}</a>
						<p>first 10 row of document first 10 row of document first 10
							row of document</p>
					</div>
					<div class="hr"></div>


				</c:forEach>


			</div>

			<div class="hh">هنحط هنا اى صوره او فيديو كدا انميشن</div>




		</div>
	</section>
	<script
		src="${pageContext.request.contextPath}/resources/js/jquery-1.11.1.min.js"></script>

	<script src="${pageContext.request.contextPath}/resources/js/custom.js"></script>

</body>