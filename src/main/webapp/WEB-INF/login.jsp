<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script defer src="https://use.fontawesome.com/releases/v5.0.6/js/all.js"></script>
<link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.1/css/bulma.min.css">
<link rel="stylesheet" type="text/css" href="/css/formstyle.css">
<title>Insert title here</title>
</head>
<body>
	<section class="hero is-warning">
	  <div class="hero-body">
	    <div class="container">
	      <h1 class="title">
	        Food On My Mind!
	      </h1>
	      <!--  <div class="image is-75x75">
			   <img src="images/image1.png">
			</div>-->
	    </div>
	  </div>
	</section>
    		<div class="wrapper-2">
				<h1 class="subtitle is-2">Sign In 
					<i class="fas fa-sign-in-alt"></i>
				</h1>
				<div  class="is-three-fifths">
					<div class="column">
						<form action="/login" method="POST">
						<div class="field">
						  <p class="control has-icons-left has-icons-right">
						    <input class="input" name="username" placeholder="Email">
						    <span class="icon is-small is-left">
						      <i class="fas fa-envelope"></i>
						    </span>
						    <span class="icon is-small is-right">
						      <i class="fas fa-check"></i>
						    </span>
						  </p>
					</div>
					<div class="field">
						<p class="control has-icons-left">
						    <input class="input" type="password" name="password" placeholder="Password">
						    <span class="icon is-small is-left">
						      <i class="fas fa-lock"></i>
						    </span>
						</p>
					</div>
					<div class="field">
					  	<p class="control">
					  		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
						    <button type="submit" class="button is-primary">
						      Login
						    </button>
					  	</p>
					</div>
					</form>
					</div>
				</div>
				<c:if test="${errorMessage != null}">
        			<p class="errors">${errorMessage}</p>
    			</c:if>
		</div>
		
    <c:if test="${logoutMessage != null}">
        <c:out value="${logoutMessage}"></c:out>
    </c:if>
</body>
</html>