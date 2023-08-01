# plan-forecast
三层框架构建模板

基于web层，service，infrastructure层的构建，层次间的依赖自上而下，但是infrastructure采用spi的形式反向依赖service，他们之间通过repository接口仓库来进行联系，entity实体和获取数据的接口都放在这里，然后由infrastructure去实现这些接口，并且提供数据存储与查询。

- web：对接前段，存放api，controller，vo，convertvo等，承接前端请求与service业务逻辑处理
- service：核心业务代码，负责各种业务处理，从infrastructure获取数据
- infrastructure：负责从各个数据存储中间件，fegin调用，rpc框架获取数据，转换为entity供service层使用
