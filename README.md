<<<<<<< HEAD
## hotswap

**热更新代码，不需要停止服务器，不需要额外的任何配置，一行代码开启热更新**

**将需要热更新的文件编译后放入resources中**

采用javassist和Byte Buddy两种方案,默认是执行javassist

```java
热更新教程，需要添加JVM参数，-Djdk.attach.allowAttachSelf=true，如果不加这个参数将使用Byte Buddy热更新替代Javassist热更新
```
JVM热更新的局限


基于Attach机制实现的热更新，更新类需要与原来的类在包名，类名，修饰符上完全一致，否则在classRedefine过程中会产生classname  don't match 的异常。


例如显示这样的报错：redefineClasses exception class redefinition failed: attempted to  delete a method.


具体来说，JVM热更新局限总结：


函数参数格式不能修改，只能修改函数内部的逻辑

不能增加类的函数或变量

函数必须能够退出，如果有函数在死循环中，无法执行更新类(笔者实验发现，死循环跳出之后，再执行类的时候，才会是更新类)

## protocol

### 自定义协议

自定义集合,异常处理,类处理,IO流,文件处理等工具类
防止工具类被实例化,全部定义为抽象类

## util

=======
## hotswap

**热更新代码，不需要停止服务器，不需要额外的任何配置，一行代码开启热更新**

**将需要热更新的文件编译后放入resources中**

采用javassist和Byte Buddy两种方案,默认是执行javassist

```java
热更新教程，需要添加JVM参数，-Djdk.attach.allowAttachSelf=true，如果不加这个参数将使用Byte Buddy热更新替代Javassist热更新
```



## protocol

**自定义协议**

自定义集合,异常处理,类处理,IO流,文件处理等工具类

## util

>>>>>>> origin/master
