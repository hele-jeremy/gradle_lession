Appjoint:https://juejin.cn/post/6844903687488274445
该方案通过字节码插桩的形式将我们在子模块中定义的Application
在编译时期,动态的添加到自定义的Application生命周期派发类中
实现了,集成模式运行时会将主App入口的Application生命周期派发给
我们各个业务模块中的自定义的Application类
例如集成运行时候:
        AppBase -> App
                    -> Module2Application  会有优先级
                    -> Module1Application
   注意:这种方式在集成模式运行时候例如Module2Application中的onCreate()方法中如果有super.onCreate()
   那么会导致父类的AppBase的onCreate方法执行两次,如果此时在AppBase中执行了一些基础库的初始化操作
   那么需要注意重复初始化的问题,可以去除掉子Application中的super.onCreate调用，或者通过
   app-startup这种辅助初始化的类来避免重复初始化的问题

单独运行情况下，单独独立运行的业务组件模块,可以通过继承业务组件的Application例如Module2Application
Module2Application中执行的是当前module2组件需要执行的一些初始化，而继承业务组件的Application中可以
做一些单独独立运行时候的初始化,比如mock一些数据的操作等等

https://www.jianshu.com/p/65433846d38a
而该方式则是通过一个委托代理接口来进行生命周期的分发


apt processor 远程调试:https://blog.csdn.net/u012823070/article/details/89237170

//gradle transform  + asm 
https://asm.ow2.io/developer-guide.html（ASM官网）
https://juejin.cn/post/68449037809163960393
https://add7.cc/android-base/shi-yong-asm-jin-hang-zi-jie-ma-cha-zhuang
https://juejin.cn/post/6844904118700474375#heading-24
https://juejin.cn/post/6844903473599741960
https://juejin.cn/post/6844904072370192397#heading-2
https://zhuanlan.zhihu.com/p/271599264
https://www.jianshu.com/p/abba54baf617
https://blog.csdn.net/aesop_wubo/article/details/48948211（Opcodes.ALOAD,Opcodes.ILOAD指令详解）
https://zhuanlan.zhihu.com/p/94498015?hmsr=toutiao.io（ASM子节码的介绍）
https://joeskye1701.github.io/ASM/
https://www.cnblogs.com/yungyu16/p/13172207.html
https://jishuin.proginn.com/p/763bfbd36680
https://juejin.cn/post/6927206349260636173#heading-5
https://mp.weixin.qq.com/s/dQjsxduUiNrMYH2xhhpmQA




