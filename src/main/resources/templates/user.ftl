<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }

        th, td {
            padding: 5px;
        }

        th {
            text-align: left;
        }
    </style>
</head>
<body>
<table>
    <tr>
        <th>用户id</th>
        <th>余额</th>
    </tr>
    <tr>
        <td>${user_id?c}</td>
        <td>${user_balance}</td>
    </tr>
</table>


<h1>我收到的</h1>
<table>
    <tr>
        <th>来自</th>
        <th>红包金额</th>
        <th>红包详情</th>
    </tr>
    <#list user_received as received>
        <tr>
            <th>${received.packetEnvelope.user.userId}</th>
            <th>${received.packetValue}</th>
            <th><a href="/packet/${received.packetId?c}">红包详情</a></th>
        </tr>
    </#list>
</table>

<h1>我发出的</h1>
<a href="/user/send?user_id=${user_id?c}">发红包</a>

<table>
    <tr>
        <th>红包id</th>
        <th>红包金额</th>
        <th>红包个数</th>
    </tr>
    <#list user_send as send>
        <tr>
            <th>${send.envelopeId?c}</th>
            <th>${send.envelopeSum}</th>
            <th>${send.envelopeCount}</th>
            <th><a href="/envelope/detail/${send.envelopeId?c}">红包详情</a></th>
        </tr>
    </#list>
</table>

</body>
</html>