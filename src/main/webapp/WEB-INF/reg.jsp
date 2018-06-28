<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
	<div class="reg_main">
		<h1 class="subtitle is-2">Create an Account</h1>
		 
	    <form:form method="POST" action="/registration" modelAttribute="user">
	    	<div class="field">
	    		<p class="control has-icons-left">
		            <form:input path="alias" class="input" type="text" placeholder="Username"/>
		            <span class="icon is-small is-left">
		      			<i class="fas fa-user"></i>
		    		</span>
		            <form:errors path="alias" class="errors"/>
	            </p>
	        </div>
	        <div class="field">
		 		<p class="control has-icons-left has-icons-right">
		            <form:input path="username" class="input" type="email" placeholder="Email"/>
		            <span class="icon is-small is-left">
				      <i class="fas fa-envelope"></i>
				    </span>
				    <span class="icon is-small is-right">
				      <i class="fas fa-check"></i>
				    </span>
		            <form:errors path="username" class="errors"/>
	        	</p>
	        </div>
	        <div class="field">
	    		<p class="control has-icons-left">
		            <form:password path="password" class="input" placeholder="Password"/>
		            <span class="icon is-small is-left">
				      <i class="fas fa-lock"></i>
				    </span>
		            <form:errors path="password" class="errors"/>
	        	</p>
	        </div>
	        <div class="field">
	    		<p class="control has-icons-left">
	            <form:password path="passwordConfirmation" class="input"  placeholder="Confirm Password"/>
	            <span class="icon is-small is-left">
				    <i class="fas fa-lock"></i>
				</span>
	            <form:errors path="passwordConfirmation" class="errors"/>
	        	</p>
	        </div>
	        <div class="field">
			  <p class="control">
			    <button class="button is-success" type="submit">
		      		Register!
		    	</button>
		  </p>
		</div>
	    </form:form>
    </div>
</body>
</html>