# 后端部分知识总结
1.IOC与DI
    IOC全称Inversion of Control,将对象的创建转移到程序外部,由框架自动创建对象实例统一管理
    DI全称Dependency Injection,在程序运行时动态的提供IOC所创建的对象

2.PageHelper
    PageHelper是一款分页插件,调用PageHelper的静态方法startPage,自动开启下一条查询语句的分页查询,并且自动将查询到的结果封装到Page<>对象中,调用方法即可得到总数以及分页查询的结果（注意一个startPage只对一个查询语句生效）

3.事务
    事务的定义
        一组操作的集合,要么全部成功,要么全部失败,保证数据的一致性
    事务的特性：ACID
        - ‌原子性‌（Atomicity）：不可分割的最小单元
        - ‌一致性‌（Consistency）：事务前后数据状态合法
        - ‌隔离性‌（Isolation）：并发事务互不干扰
        - ‌持久性‌（Durability）：提交后数据永久生效
    事务的隔离级别
        - Read Uncommitted：几乎不用
        - Read Commiteed：可以解决脏读,大多数数据库使用（Oracle）
        - Repeatable Read：可以解决不可重复读,MySql默认
        - Serializable：可以解决幻读,最强一致性,性能较低
    Spring事务的机制
        - 实现原理
            - 基于AOP和数据库事务,通过代理对象实现控制事务的启停
        - 传播行为
            - 略
        - 失效场景
            - 方法非public
            - 自调用（未通过代理）
            - 异常被catch未抛出
        - 使用方式
            在类或者方法加@Transactional注解

4.OSS
    概念
        阿里云对象存储服务
    使用条件
        安装SDK
        引入相关依赖
        配置bucket参数和访问id及秘钥

5.全局异常处理
    对Controller层抛出的异常进行处理,定义一个单独的异常处理类,在类上添加@RestControllerAdvice注解,类中的方法添加@ExceptionHandler注解,对想要捕获的异常进行分别处理
    全局异常处理类的Code Reference:SummaryReferenceCode/GlobalExceptionHandler.java

6.JWT
    概念
        JWT（JSON Web Token）是一种基于JSON的开放标准（RFC 7519）,
用于在网络应用间安全传递信息
    编码方式
        base64
    组成部分
        - Header
            包含令牌类型和令牌签名使用的加密算法
        - Payload
            包含预定义声明如有效时间
            包含声明,内含应用特定数据
        - signature
            使用加密算法对前面两个部分进行签名,确保前面的部分不被更改
    核心特性
        - 自包含,无需服务端存储
        - 可以跨域
        - 安全
    使用方式
        在初次访问的时候向客户端返回一个JWT令牌,客户端会存储在本地,然后接下来每次请求都会携带JWT令牌,服务端对JWT令牌进行解析,如果解析不抛异常则证明令牌的有效性,以此来标识身份
        令牌的生成与解析的Code Reference:SummaryReferenceCode/JwtUtils.java

7.过滤器Filter
    概念
        Java Servlet中的三大组件之一,可以拦截所有的HTTP请求和响应,生命周期与Servlet同步
    使用方式
        定义一个Filter类,并且添加注解@WebFilter("url"),表示该过滤器拦截的请求路径,然后重写doFilter方法,方法中定义拦截相关的逻辑,通过的话再次调用doFilter方法对请求资源和返回资源进行放行
        CodeReference:SummaryReferenceCode/TokenFilter.java

8.拦截器Interceptor
    概念
        基于Spring AOP的实现,主要用于在控制器方法执行前后进行拦截处理.与Filter不同,Interceptor工作在Spring MVC层面,能够直接访问Spring上下文中的对象.
    使用方式
        首先需要实现HandlerInterceptor接口,然后配置一个实现WebMvcConfigurer的配置类中,重写addInterceptors方法,对自定义的拦截器进行注册

9.Spring AOP
    概念
        面向切面编程(Aspect Oriented Programming)是一种编程思想,通过横向抽取机制将分散在各处的公共逻辑集中处理
    核心组件 
        切面(Aspect)‌：封装横切关注点的模块（如日志模块）
        连接点(Join Point)‌：程序执行过程中的特定点（Spring AOP中指方法执行）
        切点(Pointcut)‌：通过表达式定义的一组连接点（如匹配所有Service方法）
        通知(Advice)‌：在切点执行的增强逻辑
    通知类型
        一共五种:
            @Around:包含以下四个
            @Before:执行之前
            @After:执行之后
            @AfterReturning:返回之后
            @AfterThrowing:抛异常之后
    切点表达式
        execution表达式:
            execution([修饰符] 返回类型 包名.类名.方法名(参数))
        注解方式
            @annotation(*) 自定义的注解
            @within(org.springframework.stereotype.Service) 匹配Service注解类中的所有方法
        可以使用&&和||和!对以上两种进行运算
    实现原理
        代理机制
            JDK动态代理:针对接口实现,基于反射创建对象
            CGLIB(default):针对类实现,继承得到子类,无法代理final类和方法
    使用方式
        定义一个切面类@Aspect,然后在类中定义不同的通知类型,指定不同的切入点,执行对应的方法体
        CodeReference:SummaryReferenceCode/OperationLogAspect.java