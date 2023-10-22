# 上报

## Login with password

此数据包由客户端发送，请求代理登录到服务器

|  类型  |      名称       |     
|:----:|:-------------:|
| login | login_with_password |

|  字段名   |  要求的值   |     
|:------:|:-------:|
| uid | 用户ID |       
| password | 密码 |       

# 推送

## Login success

此数据包由代理发送，提示客户端登录请求已经成功

|  类型  |      名称      |     
|:----:|:------------:|
| login | login_success |

|  字段名   | 可能的值 |     
|:------:|:----:|
| uid | 用户ID |       
| token | 登录token |       
