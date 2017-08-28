<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<a href="springmvc/testRedirect">Test Redirect</a>
	<br><br>
	<a href="springmvc/testView">Test View</a>
	<br><br>
	<a href="springmvc/testViewAndViewResolver">Test ViewAndViewResolver</a>
	<br><br>
	<form action="springmvc/testModelAttribute" method="post">
		<input type="hidden" name="id" value="1"/>
		username:<input type="text" name="username" value="Tom"/><br>
		email:<input type="text" name="email" value="tom@sina.com"/><br>
		age:<input type="text" name="age" value="12"/><br>
		<input type="submit" value="Submit"/>
	</form>
	<br><br>
	<a href="springmvc/testSessionAttributes">Test SessionAttributes</a>
	<br><br>
	<a href="springmvc/testMap">Test Map</a>
	<br><br>
	<a href="springmvc/testModelAndView">Test ModelAndView</a>
	<br><br>
	<a href="springmvc/testServletApi">Test ServletApi</a>
	<br><br>
	<form action="springmvc/testPojo" method="post">
		username:<input type="text" name="username"/>
		<br>
		password:<input type="password" name="password"/>
		<br>
		age:<input type="text" name="age"/>
		<br>
		email:<input type="text" name="email"/>
		<br>
		city:<input type="text" name="address.city"/>
		<br>
		province:<input type="text" name="address.province"/>
		<br>
		<input type="submit" value="Submit"/>
	</form>
	<br><br>
	<a href="springmvc/testCookieValue">Test CookieValue</a>
	<br><br>
	<a href="springmvc/testRequestHeader">Test RequestHeader</a>
	<br><br>
	<a href="springmvc/testRequestParam?username=z3&age=10">Test RequestParam</a>
	<br><br>
	<a href="springmvc/testRest/2">Test RestGet</a>
	<br><br>
	<form action="springmvc/testRest" method="post">
		<input type="submit" value="Test Rest Post"/>	
	</form>
	<br><br>
	<form action="springmvc/testRest/2" method="post">
		<input type="hidden" name="_method" value="PUT"/>
		<input type="submit" value="Test Rest put"/>	
	</form>
	<br><br>
	<form action="springmvc/testRest/2" method="post">
		<input type="hidden" name="_method" value="DELETE"/>
		<input type="submit" value="Test Rest delete"/>	
	</form>
	<br><br>
	<a href="springmvc/testPathVariable/2">Test PathVariable</a>
	<br><br>
	<a href="springmvc/testParamAndHeader?username=z3&age=11&gender=1">Test ParamAndHeader</a>
	<br><br>
	<form action="springmvc/testMethod" method="post">
		<input type="submit" value="Submit"/>
	</form>
	<br><br>
	<a href="springmvc/testMethod">Test Method</a>
	<br><br>
	<a href="springmvc/testRequestMapping">Test RequestMapping</a>
	<br><br>
	<a href="helloWorld">Hello World</a>

</body>
</html>