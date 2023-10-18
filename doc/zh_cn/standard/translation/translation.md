以下是一个post示例

```json
{
    "post_type": "meta",
    "post_name": "proxy_connect",
    "data": {
        "cipher": "-"
    }
}
```

其中 post_type、post_name、data是在每次post都存在的

data里面的内容随着type和name变化而变化，具体请见：[数据包翻译列表](/doc/zh_cn/standard/translation/list)