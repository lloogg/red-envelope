<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>发红包</title>
</head>
<body>
<form action="/user/${userId?c}/send" method="get">
    <label for="sum">红包总额：</label>
    <input type="text" id="sum" name="sum"><br>
    <label for="count">红包数量：</label>
    <input type="text" id="count" name="count"><br>
    <input type="submit">
</form>
</body>
</html>