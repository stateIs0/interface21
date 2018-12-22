THE SPRING FRAMEWORK, release 0.9.1 (August 2003)
-------------------------------------------------
http://www.springframework.org


#   1. INTRODUCTION 介绍

Spring is a J2EE application framework based on code published in "Expert One-on-One J2EE Design and Development" by Rod Johnson (Wrox, 2002).

Spring是一个J2EE应用程序框架，它基于Rod Johnson的“Expert One-on-One J2EE设计和开发”中发布的代码（Wrox，2002）。

Spring includes:
* Powerful JavaBeans-based configuration management, applying Inversion-of-Control principles. This makes wiring up applications quick and easy. No more singletons littered throughout your codebase; no more arbitrary properties file. One consistent and elegant approach everywhere.
* JDBC abstraction layer that offers a meaningful exception hierarchy (no more pulling vendor codes out of SQLException), simplifies error handling, and greatly reduces the amount of code you'll need to write. You'll never need to write another finally block to use JDBC again.
* Similar abstraction layer for transaction management, allowing for pluggable transaction managers, and making it easy to demarcate transactions without dealing with low-level issues. Strategies for JTA and a single JDBC DataSource are included.
* Integration with Hibernate and JDO, in terms of resource holders, DAO implementation support, and transaction strategies. First-class Hibernate support with lots of IoC convenience features, solving many typical Hibernate integration issues.
* AOP functionality, fully integrated into Spring configuration management. You can AOP-enable any object managed by Spring, adding aspects such as declarative transaction management. With Spring, you can have declarative transaction management without EJB... even without JTA, if you're using a single database in Tomcat or another web container without JTA support.
* Flexible MVC web application framework, built on core Spring functionality. This framework is highly configurable and accommodates multiple view technologies.

* 强大的基于JavaBeans的配置管理，应用Inversion-of-Control原则。这使得应用程序的连接变得快速而简单。整个代码库中没有更多的单例;没有更多的任意属性文件。无处不在的优雅方法。
* JDBC抽象层，提供有意义的异常层次结构（不再从SQLException中提取供应商代码），简化了错误处理，并大大减少了您需要编写的代码量。您永远不需要再编写另一个finally块来再次使用JDBC。
* 用于事务管理的类似抽象层，允许可插入的事务管理器，并且可以在不处理低级别问题的情况下轻松划分事务。包括JTA策略和单个JDBC数据源。
* 在资源持有者，DAO实施支持和交易策略方面与Hibernate和JDO集成。一流的Hibernate支持，具有许多IoC便利功能，解决了许多典型的Hibernate集成问题。
* AOP功能，完全集成到Spring配置管理中。您可以通过AOP启用Spring管理的任何对象，添加声明式事务管理等方面。使用Spring，您可以在没有EJB的情况下进行声明式事务管理...即使没有JTA，如果您在Tomcat中使用单个数据库，或者在没有JTA支持的情况下使用其他Web容器。
* 灵活的MVC Web应用程序框架，基于核心Spring功能。该框架具有高度可配置性，可适应多种视图技术。

You can use all of Spring's functionality in any J2EE server, and most of it in non-managed environments too. A central focus of Spring is to allow for reusable business and data access objects that are not tied to specific J2EE services. Such objects can be used in J2EE environments with or without EJB, standalone applications, test environments, etc without any hassle.

您可以在任何J2EE服务器中使用Spring的所有功能，并且大多数功能也可以在非托管环境中使用。 Spring的一个重点是允许可重用​​的业务和数据访问对象与特定的J2EE服务无关。这些对象可以在有或没有EJB，独立应用程序，测试环境等的J2EE环境中使用，而不会有任何麻烦。

Spring has a layered architecture. All its functionality builds on lower levels. So you can e.g. use the JavaBeans configuration management without using the MVC framework or AOP support. But if you use the MVC framework or AOP support, you'll find they build on the configuration framework, so you can apply your knowledge about it immediately.

Spring有一个分层的架构。它的所有功能都建立在较低级别上。所以你可以，例如使用JavaBeans配置管理而不使用MVC框架或AOP支持。但是如果您使用MVC框架或AOP支持，您会发现它们构建在配置框架上，因此您可以立即应用您的知识。

# 2. RELEASE INFO

The Spring Framework is released under the terms of the Apache Software License (see license.txt). This is the second public release towards 1.0. 

Spring Framework是根据Apache软件许可证的条款发布的（请参阅license.txt）。这是1.0的第二次公开发布。

The Spring Framework requires J2SE 1.3 and J2EE 1.3 (Servlet 2.3, JSP 1.2, JTA 1.0, EJB 2.0). Integration is provided with Log4J 1.2, CGLIB 1.0, Hibernate 2.0, JDO 1.0, Caucho's Hessian/Burlap 2.1/3.0, JSTL 1.0, Velocity 1.3, and more.

Spring Framework需要J2SE 1.3和J2EE 1.3（Servlet 2.3，JSP 1.2，JTA 1.0，EJB 2.0）。集成提供Log4J 1.2，CGLIB 1.0，Hibernate 2.0，JDO 1.0，Caucho的Hessian / Burlap 2.1 / 3.0，JSTL 1.0，Velocity 1.3等。

Note: This release uses "com.interface21" as root package, like the original version that came with the book. For the sake of naming consistency, the root package name will change to "org.springframework", starting with 1.0 milestone releases.

备注: 此版本使用“com.interface21”作为根包，就像本书附带的原始版本一样。为了命名一致性，根包名称将更改为“org.springframework”，从1.0里程碑版本开始。

Release contents:
* "src" contains the Java source files; 包含Java源文件
* "dist" contains various Spring Jar files; 包含各种Spring Jar文件
* "lib" contains the most important third-party libraries; 包含最重要的第三方库
* "docs" contains general and API documentation; 包含一般和API文档
* "samples" contains demo application and skeletons. 包含演示应用程序和骨架

Latest info is available at the public website: http://www.springframework.org
Project info at the SourceForge site: http://sourceforge.net/projects/springframework

有关最新信息，请访问公共网站：http：//www.springframework.org

SourceForge站点上的项目信息：http：//sourceforge.net/projects/springframework

This product includes software developed by the Apache Software Foundation (http://www.apache.org).

本产品包含由Apache Software Foundation（http://www.apache.org）开发的软件。


# 3. WHERE TO START?

Preliminary documentation can be in the "docs" directory. Documented application skeletons can be found in "samples/skeletons". Demo applications can be found in "samples/petclinic" and "samples/countries" (with their own readme.txt).

初步文档可以在“docs”目录中。记录的应用程序骨架可以在“样本/骨架”中找到。演示应用程序可以在“samples / petclinic”和“samples / countries”（带有自己的readme.txt）中找到。

Note: The tutorial in "docs" is a work in progress. It is not fully consistent with the current state of the framework but is still useful as introduction.

注意：“docs”中的教程是一项正在进行的工作。它与框架的当前状态不完全一致，但仍然可用作引言。

The "dist" directory contains the following (overlapping) Jar files for use in applications:

“dist”目录包含以下（重叠）Jar文件，供应用程序使用：

* "spring-beans": JUST THE BEAN CONTAINER; 只是 Bean 容器
* "spring-jdbc": bean container, AOP framework, transaction framework, JDBC support, O/R Mapping support;bean容器，AOP框架，事务框架，JDBC支持，O / R Mapping支持;
* "spring-full": all Spring framework classes.所有Spring框架类。

"Expert One-on-One J2EE Design and Development" discusses many of Spring's design ideas in detail. Note: The code examples in the book refer to the original framework version that came with the book. Thus, they may need to be adapted to work with the current Spring release.

“Expert One-on-One J2EE设计与开发”详细讨论了Spring的许多设计思想。注意：本书中的代码示例引用了本书附带的原始框架版本。因此，它们可能需要适应当前的Spring版本。

本人微信，欢迎一起探讨：

![image](https://user-images.githubusercontent.com/24973360/50372024-5f975d00-0601-11e9-8247-139e145b1123.png)
