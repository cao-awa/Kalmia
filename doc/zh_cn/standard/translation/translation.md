以下是一则数据包示例

```json
{
    "post_type": "meta",
    "post_name": "login_with_password",
    "time": 1697935934367,
    "identity": "bD2skmV6loZOIki8jaGWdfv6",
    "data": {
        "uid": 114514,
        "pwd": "abc123xxx"
    }
}
```

其中 post_type 和 post_name 用以区分需要处理的是哪样的数据\
time 是毫秒时间戳（UNIX）\
identity 是基于客户端的唯一标识\
data 则是此数据包的负载内容，即数据包详情内容

data里面的内容随着type和name变化而变化，具体请见：[数据包翻译列表](/doc/zh_cn/standard/translation/list)
