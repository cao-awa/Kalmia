# 上报

## Proxy connect

此数据包在客户端每次连接到代理时由客户端发送，提示服务端可以开始推送消息

|  类型  |      名称       |     
|:----:|:-------------:|
| Meta | proxy_connect |

|  字段名   |  要求的值   |     
|:------:|:-------:|
| cipher | 空或指定字符串 |       

# 推送

## Proxy status

此数据包由代理发送，提示客户端当前代理的状态

|  类型  |      名称      |     
|:----:|:------------:|
| Meta | proxy_status |

|  字段名   | 可能的值 |     
|:------:|:----:|
| status | 状态键名 |       

|             状态键名              |        意味        |     
|:-----------------------------:|:----------------:|
|   status.kalmia.connecting    |   正在连接到山月桂服务器    |
|  status.kalmia.disconnected   |   与山月桂服务器断开连接    |
|  status.kalmia.handshake.ec   | 正在进行握手（椭圆曲线加密阶段） |
| status.kalmia.handshake.hello | 正在进行握手（Hello阶段）  |
|      status.kalmia.login      |       正在登录       |
