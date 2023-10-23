# 上报

## Login with password

此数据包由客户端发送，请求代理登录到服务器

|  类型   |         名称          |     
|:-----:|:-------------------:|
| login | login_with_password |

| 字段名 | 要求的值 |     
|:---:|:----:|
| uid | 用户ID |       
| pwd |  密码  |       

# 推送

## Login success

此数据包由代理发送，提示客户端登录请求已经成功

|  类型   |      名称       |     
|:-----:|:-------------:|
| login | login_success |

|  字段名  |  可能的值   |     
|:-----:|:-------:|
|  uid  |  用户ID   |       
| token | 登录token |       

## Login failure

此数据包由代理发送，提示客户端登录请求已经失败

|  类型   |      名称       |     
|:-----:|:-------------:|
| login | login_failure |

|  字段名   | 可能的值 |     
|:------:|:----:|
|  uid   | 用户ID |       
| reason | 失败原因 |       

|                原因键名                 |    意味    |     
|:-----------------------------------:|:--------:|
|  login.failure.pwd_or_uid_is_wrong  | 密码或UID错误 |
| login.failure.unable_to_verify_sign |  无法验证签名  |
|     login.failure.invalid_token     | 无效的Token |
|  login.failure.handshake_required   |  未完成握手   |
