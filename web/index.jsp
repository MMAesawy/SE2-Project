<%--
  Created by IntelliJ IDEA.
  User: MMAesawy
  Date: 2020-03-23
  Time: 11:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<form action="api/register" method="post">
  Enter username:<input type="text" name="username"/><br/><br/>
  Enter email:<input type="text" name="email"/><br/><br/>
  Enter password:<input type="text" name="password"/><br/><br/>
  <input type="submit" value="Register"/>
</form>

<form action="api/login" method="post">
  Enter Id:<input type="text" name="identifier"/><br/><br/>
  Enter Name:<input type="text" name="password"/><br/><br/>
  <input type="submit" value="Login"/>
</form>

</html>
