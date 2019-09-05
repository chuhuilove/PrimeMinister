# Prime Minister (宰相)


宰相肚里能撑船 


## 帧协议

```
分隔符  + 命令 +   响应体长度  +命令+   真实数据                                 分隔符
<PMDB>    +       body     + set/get + actual data google protocol buffer压缩 + </PMDB>
6         6      + int类型            长度                                   +  7个字节

<PMDB>1024 <+>命令</+> <PMDB>
6+4+
```








