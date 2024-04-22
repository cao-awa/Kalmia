# 消息对象

消息对象有以下数据，存在的字段由type字段决定

## Default message

|  字段名   |   可能的值    | 类型  |
|:------:|:---------:|:---:|
|  type  | "default" | 字符串 |
|  text  |   消息内容    | 字符串 |
| digest |    摘要     | 字符串 |
| sender |   发送者ID   | 数字  |
| signed |   是否签名    | 布尔值 |

## Resigned message

> 此部分未完善

|       字段名       |    可能的值    | 类型  |
|:---------------:|:----------:|:---:|
|      type       | "resigned" | 字符串 |
|  resigned_text  |  重签名消息内容   | 字符串 |
| resigned_digest |   重签名摘要    | 字符串 |
| resigned_sender |  重签名发送者ID  | 数字  |
|   source_text   |   原始消息内容   | 字符串 |
|  source_digest  |    原始摘要    | 字符串 |
|  source_sender  |  原始发送者ID   | 数字  |
|  source_signed  |  原始消息是否签名  | 布尔值 |

## Untrusted message

所谓untrusted message，即不进行加密也未进行签名的消息，这类消息属于不可信的

|  字段名   |    可能的值     | 类型  |
|:------:|:-----------:|:---:|
|  type  | "untrusted" | 字符串 |
|  text  |    消息内容     | 字符串 |
| digest |     摘要      | 字符串 |
| sender |    发送者ID    | 数字  |

# 上报

## Send message

此数据包由客户端发送，用于发送消息

|   类型    |      名称      |     
|:-------:|:------------:|
| message | send_message |

|     字段名      |  要求的值  | 类型  |     
|:------------:|:------:|:---:|
|     sid      |  会话ID  | 数字  |
|   receipt    |  回执数据  | 字符串 |
|  need_sign   | 是否需要签名 | 布尔值 |
| need_encrypt | 是否需要加密 | 布尔值 |
|     msg      |  消息内容  | 字符串 |

> 其中receipt要求为16字符的字符串，可以由任何字符组成，保证不会重复出现即可

## Select message

|   类型    |       名称       |     
|:-------:|:--------------:|
| message | select_message |

| 字段名  | 要求的值 |          类型          |
|:----:|:----:|:--------------------:|
| sid  | 会话ID | LongAndExtraIdentity |
| from | 起始序列 |          数字          |
|  to  | 结束序列 |          数字          |

# 推送

## New message notice

此数据包由代理发送，提示客户端收到了新的消息提醒

|   类型    |         名称         |     
|:-------:|:------------------:|
| message | new_message_notice |

| 字段名 | 可能的值 |          类型          |
|:---:|:----:|:--------------------:|
| sid | 会话ID | LongAndExtraIdentity |
| seq | 序列号  |          数字          |
| msg | 消息内容 |         消息对象         |

## Selected message

此数据包由代理发送，是 ``select_message`` 的反馈数据包

|   类型    |        名称        |     
|:-------:|:----------------:|
| message | selected_message |

|   字段名    | 可能的值 |          类型          |
|:--------:|:----:|:--------------------:|
|   sid    | 会话ID | LongAndExtraIdentity |
|   from   | 起始序列 |          数字          |
|    to    | 结束序列 |          数字          |
| messages | 消息列表 |      JSONArray       |