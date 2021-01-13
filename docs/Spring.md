# Spring Framework

Spring Framework 主要包括几个模块：

1. 支持 IoC 和 AOP 的容器；
2. 支持 JDBC 和 ORM 的数据访问模块；
3. 支持声明式事务的模块；
4. 支持基于 Servlet 的 MVC 开发；
5. 支持基于 Reactive 的 Web 开发；
6. 以及集成 JMS、JavaMail、JMX、缓存等其他模块。

## IOC

IoC 全称 Inversion of Control，直译为控制反转

IoC 又称为依赖注入（DI：Dependency Injection），它解决了一个最主要的问题：
将组件的创建+配置与组件的使用相分离，并且，由 IoC 容器负责管理组件的生命周期。

### scope

对于 Spring 容器来说，当我们把一个 Bean 标记为`@Component` 后，
它就会自动为我们创建一个单例（Singleton），即容器初始化时创建 Bean，
容器关闭前销毁 Bean。在容器运行期间，我们调用 getBean(Class)获取到的 Bean 总是同一个实例。

还有一种 Bean，我们每次调用 getBean(Class)，容器都返回一个新的实例，
这种 Bean 称为 Prototype（原型），它的生命周期显然和 Singleton 不同。
声明一个 Prototype 的 Bean 时，需要添加一个额外的`@Scope` 注解：

```java
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // @Scope("prototype")
public class MailSession {
    ...
}
```

### 注入 List

在需要注入某些实现同一借口的类的时候（比如校验类 Validator）可以通过注入一个
`List<Validator>` 装配所有的实现类。

因为 Spring 是通过扫描 classpath 获取到所有的 Bean，而 List 是有序的，
要指定 List 中 Bean 的顺序，可以加上`@Order`注解：

```java
@Autowired
List<Validator> list;

public void validate(String email, String password, String name) {
  for (var validator : this.validators) {
    validator.validate(email, password, name);
  }
}
```

### 可选注入

默认情况下，当我们标记了一个@Autowired 后，Spring 如果没有找到对应类型的 Bean，
它会抛出 NoSuchBeanDefinitionException 异常。
可以给@Autowired 增加一个 `required = false` 的参数
这种方式非常适合有定义就使用定义，没有就使用默认值的情况

### 创建第三方 Bean

如果一个 Bean 不在我们自己的 package 管理之内，例如 ZoneId，如何创建它？
答案是我们自己在@Configuration 类中编写一个 Java 方法创建并返回它，
注意给方法标记一个@Bean 注解：

```java
@Configuration
@ComponentScan
public class AppConfig {
    // 创建一个Bean:
    @Bean
    ZoneId createZoneId() {
        return ZoneId.of("Z");
    }
}
```

### 初始化和销毁

有些时候，一个 Bean 在注入必要的依赖后，需要进行初始化（监听消息等）。
在容器关闭时，有时候还需要清理资源（关闭连接池等）。
我们通常会定义一个 init()方法进行初始化，定义一个 shutdown()方法进行清理，
然后，引入 JSR-250 定义的 Annotation：

```xml
<dependency>
    <groupId>javax.annotation</groupId>
    <artifactId>javax.annotation-api</artifactId>
    <version>1.3.2</version>
</dependency>
```

在 Bean 的初始化和清理方法上标记`@PostConstruct`和`@PreDestroy`

```java
@Component
public class MailService {
    @Autowired(required = false)
    ZoneId zoneId = ZoneId.systemDefault();

    @PostConstruct
    public void init() {
        System.out.println("Init mail service with zoneId = " + this.zoneId);
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("Shutdown mail service");
    }
}
```

Spring 容器会对上述 Bean 做如下初始化流程：

1. 调用构造方法创建 MailService 实例；
2. 根据@Autowired 进行注入；
3. 调用标记有@PostConstruct 的 init()方法进行初始化。

而销毁时，容器会首先调用标记有`@PreDestroy` 的 `shutdown()`方法。
Spring 只根据 Annotation 查找无参数方法，对方法名不作要求。

### 别名

可以用`@Bean("name")`指定别名，也可以用`@Bean+@Qualifier("name")`指定别名。
注入标记有@Primary 的 Bean。这种方式也很常用。
例如，对于主从两个数据源，通常将主数据源定义为@Primary

### 使用 FactoryBean

Spring 也提供了工厂模式，允许定义一个工厂，然后由工厂创建真正的 Bean。
用工厂模式创建 Bean 需要实现 FactoryBean 接口

```java
@Component
public class ZoneIdFactoryBean implements FactoryBean<ZoneId> {

    String zone = "Z";

    @Override
    public ZoneId getObject() throws Exception {
        return ZoneId.of(zone);
    }

    @Override
    public Class<?> getObjectType() {
        return ZoneId.class;
    }
}
```

当一个 Bean 实现了 FactoryBean 接口后，Spring 会先实例化这个工厂，
然后调用 getObject()创建真正的 Bean。getObjectType()可以指定创建的 Bean 的类型，
因为指定类型不一定与实际类型一致，可以是接口或抽象类。
因此，如果定义了一个 FactoryBean，
要注意 Spring 创建的 Bean 实际上是这个 FactoryBean 的 getObject()方法返回的 Bean。
为了和普通 Bean 区分，我们通常都以 `XxxFactoryBean` 命名。

