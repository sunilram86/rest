<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="com.test.ItemStore" %>

<html>
<head><title>Item Store</title></head>
<body>
  All the latest items!
  <form action="post" method="post">
    <ul>
      <%
        for(Object obj : ItemStore.getItems()) {
          JSONObject item = (JSONObject)obj;
      %>
          <li>
            <input type="checkbox" name="cb<%=item.get("id")%>" />
            <%=item.get("name")%>&nbsp;$<%=item.get("price")%>
      <%
        }
      %>
    </ul>
    <input type="submit" value="Add to cart!" />
  </form>
</body>
</html>
