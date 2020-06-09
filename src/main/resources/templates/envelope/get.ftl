<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>红包详情</title>
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
<#if success=="true">
    <table>
        <tr>
            <th>用户id</th>
            <th>红包金额</th>
        </tr>
        <#list record as k,v>
            <tr>
                <th>${k}</th>
                <th>${v}</th>
            </tr>
        </#list>

    </table>

</#if>
</body>
</html>