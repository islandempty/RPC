## hotswap

**热更新代码，不需要停止服务器，不需要额外的任何配置，一行代码开启热更新**

**将需要热更新的文件编译后放入resources中**

采用javassist和Byte Buddy两种方案,默认是执行javassist

```java
热更新教程，需要添加JVM参数，-Djdk.attach.allowAttachSelf=true，如果不加这个参数将使用Byte Buddy热更新替代Javassist热更新
```



## protocol

### 自定义协议

自定义集合,异常处理,类处理,IO流,文件处理等工具类

## util

