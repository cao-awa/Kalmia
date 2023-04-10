# 数据包ID

请以 [Base256](/doc/zh_cn/standard/transport/number/number_encode_standard.md) 进行数字编码

# ID保留

Kalmia主仓库保留ID序列```0```~```2147483647```，即：[0, 0, 0, 0, 127, -1, -1, -1]

任何来自其他分支的自定义数据包请从 ```2147483648```~```9223372036854775807```挑选ID