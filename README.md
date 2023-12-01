# tsl_first
外卖管理系统（前后端）
### 1.项目概述

本项目是专门为餐饮企业（餐厅、饭店）定制的一款软件产品，包括 系统管理后台 和 移动端应用 两部分。其中系统管理后台主要提供给餐饮企业内部员工使用，可以对餐厅的分类、菜品、套餐、订单、员工等进行管理维护。移动端应用主要提供给消费者使用，可以在线浏览菜品、添加购物车、下单等。

| 功能实现                                                     |
| ------------------------------------------------------------ |
| 实现基本需求，其中移动端应用通过H5实现，用户可以通过手机浏览器访问 |
| 针对移动端应用进行改进，使用微信小程序实现，用户使用起来更加方便 |
| 针对系统进行优化升级，提高系统的访问性能                     |

**1). 管理端**


餐饮企业内部员工使用。 主要功能有: 

| 模块      | 描述                                                         |
| --------- | ------------------------------------------------------------ |
| 登录/退出 | 内部员工必须登录后,才可以访问系统管理后台                    |
| 员工管理  | 管理员可以在系统后台对员工信息进行管理，包含查询、新增、编辑、禁用等功能 |
| 分类管理  | 主要对当前餐厅经营的 菜品分类 或 套餐分类 进行管理维护， 包含查询、新增、修改、删除等功能 |
| 菜品管理  | 主要维护各个分类下的菜品信息，包含查询、新增、修改、删除、启售、停售等功能 |
| 套餐管理  | 主要维护当前餐厅中的套餐信息，包含查询、新增、修改、删除、启售、停售等功能 |
| 订单明细  | 主要维护用户在移动端下的订单信息，包含查询、取消、派送、完成，以及订单报表下载等功能 |



**2). 用户端**

移动端应用主要提供给消费者使用。主要功能有:

| 模块        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| 登录/退出   | 在移动端, 用户也需要登录后使用APP进行点餐                    |
| 点餐-菜单   | 在点餐界面需要展示出菜品分类/套餐分类, 并根据当前选择的分类加载其中的菜品信息, 供用户查询选择 |
| 点餐-购物车 | 用户选中的菜品就会加入用户的购物车, 主要包含 查询购物车、加入购物车、删除购物车、清空购物车等功能 |
| 订单支付    | 用户选完菜品/套餐后, 可以对购物车菜品进行结算支付, 这时就需要进行订单的支付 |
| 个人信息    | 在个人中心页面中会展示当前用户的基本信息, 用户可以管理收货地址, 也可以查询历史订单数据 |



### 2.技术选型

**1. 应用层**

Spring Boot： 快速构建Spring项目, 采用 "约定优于配置" 的思想, 简化Spring项目的配置开发。

Spring: 统一管理项目中的各种资源(bean), 在web开发的各层中都会用到。

SpringMVC ：是spring框架的一个模块，springmvc和spring无需通过中间整合层进行整合，可以无缝集成。

SpringSession: 主要解决在集群环境下的Session共享问题。

lombok：能以简单的注解形式来简化java代码，提高开发人员的开发效率。例如开发中经常需要写的javabean，都需要花时间去添加相应的getter/setter，也许还要去写构造器、equals等方法。

Swagger： 可以自动的帮助开发人员生成接口文档，并对接口进行测试。

**2. 数据层**

MySQL： 关系型数据库, 本项目的核心业务数据都会采用MySQL进行存储。

MybatisPlus： 本项目持久层将会使用MybatisPlus来简化开发, 基本的单表增删改查直接调用框架提供的方法即可。

**3. 工具**

git: 版本控制工具, 在团队协作中, 使用该工具对项目中的代码进行管理。

maven: 项目构建工具。

junit：单元测试工具，开发人员功能实现完毕后，需要通过junit对功能进行单元测试。

![image-20210726122825225](https://github.com/1758315106/tsl_first/assets/101967822/a20f8ce3-dd4e-446e-84fa-8a425bb2b17f)

### 3.开发环境

1. JDK 1.8
2. IntelliJ IDEA 2022.1.2
3. Maven 3.6.1
4. MySQL 5.6.22

### 4.软件部署

#### 1.数据库环境搭建

1.项目的数据库创建好了之后, 可以直接将资料中 db 直接导入到数据库中。

![image-20210726124752975](https://github.com/1758315106/tsl_first/assets/101967822/276e663c-1c29-4d23-a9e9-3fb52a24c168)

2.导入**pom.xml**文件依赖

3.引入配置在**application.yml**文件中修改为自己的本地数据库

#### 2.后台系统登录/退出功能

登录页面存放目录 /resources/backend/page/login/login.html

![image-20210726233631409](https://github.com/1758315106/tsl_first/assets/101967822/02b77295-e138-4408-a196-a08a128925eb)

直接点击右侧的退出按钮即可退出系统

![image-20210727005437531](https://github.com/1758315106/tsl_first/assets/101967822/bc8842aa-b723-478f-b019-8914beab27de)


#### 2.1完善登录功能

**添加过滤器**

**过滤器具体的处理逻辑如下：**

A. 获取本次请求的URI

B. 判断本次请求, 是否需要登录, 才可以访问

C. 如果不需要，则直接放行

D. 判断登录状态，如果已登录，则直接放行

E. 如果未登录, 则返回未登录结果



#### 3.员工模块

##### 3.1新增员工

后台系统中可以管理员工信息，通过新增员工来添加后台系统用户。点击[添加员工]按钮跳转到新增页面

![image-20210728002442334](https://github.com/1758315106/tsl_first/assets/101967822/f9d0e233-7fad-4702-9e4b-030e78a6ba89)


新增员工，其实就是将我们新增页面录入的员工数据插入到employee表。employee表中的status字段已经设置了默认值1，表示状态正常。

![image-20210728004144521](https://github.com/1758315106/tsl_first/assets/101967822/0cadccdb-709c-47d5-b1c5-c5c43dea6286)


##### 3.2启用/禁用员工账号

需要注意，只有管理员（admin用户）可以对其他普通用户进行启用、禁用操作，所以普通用户登录系统后启用、禁用按钮不显示。

当管理员admin点击 "启用" 或 "禁用" 按钮时, 调用方法statusHandle

![image-20210730012723560](https://github.com/1758315106/tsl_first/assets/101967822/e1d18803-f211-4714-be44-89986903dbca)


修改员工信息本质是一个更新操作，也就是对status状态字段进行操作。

```java
/**
 * 根据id修改员工信息
 * @param employee
 * @return
 */
@PutMapping
public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
    log.info(employee.toString());

    Long empId = (Long)request.getSession().getAttribute("employee");

    employee.setUpdateTime(LocalDateTime.now());
    employee.setUpdateUser(empId);
    employeeService.updateById(employee);

    return R.success("员工信息修改成功");
}
```



在上述过程中简单介绍了项目部署与员工功能模块的实现，剩下模块在代码中可以了解。

##### 用途

- 用户注册
- 用户登录
- 用户地址管理
- 用户订单管理
- 用户购物车
- 员工管理
- 菜品管理
- 菜品分类管理
- 套餐管理
- 员工订单管理

这算是我接触的第一个Spring Boot项目，考虑到复杂性，项目也暂未使用主从分离设计和前后端分离， 可能存在前后端登陆越界的问题。
