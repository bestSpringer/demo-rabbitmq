## MQ
**类型**
* ActiveMQ：适用于中小型企业
* kafka：大数据领域，高吞吐量，效率高，丢失数据，适用于对数据要求较低的场景
* RocketMQ：高吞吐，分布式事务
* RabbitMQ：流行，用erlang开发，点对点消费，路由，数据一致性高，不会丢失数据

**协议**
* AMQP协议
* 消息--->交换机--->队列

**命令**
* 启动：rabbitmqctl start_app
* 停止：rabbitmqctl stop_app
* 查看：rabbitmqctl status
* 查询帮助：rabbitmqctl help
* 查看集群状态：rabbitmqctl cluster_status
* ***说明：rabbit是运行在erlang环境上的一个应用，rabbitmqctl start_app是启动一个app，并不是启动erlang节点***

**web页面**
* 插件安装启动：rabbitmq-plugins enable rabbitmq_management
* 页面启动默认端口：15672(localhost:15672)
* 插件查看：rabbitmq-plugins list

**过程**
* 1.首先建立生产者与虚拟主机的连接，消息通过通道传递到交换机，生产者与虚拟主机对应，虚拟主机与每一个应用对应
* 2.虚拟主机与用户绑定（先创建用户，在创建虚拟主机，进行绑定）
* 3.生产者只管发送消息，消息可以发送给交换机，也可以直接发送给队列
* 4.消费者消费消息

**7种工作模式**
* 最后一种确认机制在最新版本中才可以使用，rpc模式在此不会提到
* 1.简单模式：点对点，直接连接direct
    * 生产：
    ```
    //通道绑定消息队列
    //参数一：队列名称：hello为消息队列名字
    //参数二：durable：定义队列是否需要持久化，true表示持久化，false服务重启后队列丢失，消息丢失，durable为true，服务重启后队列存在，但是消息丢失
    //参数三：exclusive是否为独占队列，true为独占队列（独占队列：表示当前队列只允许当前连接可用）
    //参数四：autoDelete：是否在消费完成后自动删除队列，true自动删除
    //参数五：附加参数
    channel.queueDeclare("hello", false, false, false, null);

    //发布消息：交换机名称，队列名称，额外设置，消息体
    //MessageProperties.PERSISTENT_TEXT_PLAIN 服务重启后消息依然存在
    channel.basicPublish("", "hello", null, "hello wuba".getBytes());
    channel.basicPublish("", "hello", MessageProperties.PERSISTENT_TEXT_PLAIN, "hellowuba".getBytes());
    ```

	* 消费：
    ```
    channel.queueDeclare("hello", false, false, false, null);
    //参数一：队列名称，参数二：开始消费时的自动确认机制，参数三：回调接口
    channel.basicConsume("hello", true, new DefaultConsumer(channel) {
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            System.out.println("++++++++++++++++++++" + new String(body));
        }
    });
    ```
  
* 2.工作模式work queues
    * 一对多，一个生产者生产消息，多个消费者均分消费
	* 生产：
		* 循环生产一堆消息
	* 消费：
		* 多个消费者去消费，每个消费者每次只能消费一个消息
		* 不做特殊处理情况下，消息被消费者平均消费
        * 开始消费时的自动确认机制：开启机制后，消费者会将拿到消息后，确认机制确认，但是业务处理逻辑还没执行完，消费者只管拿去消息，如果还没处理完业务，消费者挂起，会影响消息的完整性，因此应该将确认机制关闭，并且规定每次只能处理一个消息
        ```
        //每次只能消费一个消息
        channel.basicQos(1);

        channel.basicConsume("work", false, new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
              try {
                  Thread.sleep(2000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              System.out.println("消费者-1: " + new String(body));
              //参数一：确认队列中的具体消息，参数二：是否开启多个消息同时确认
              channel.basicAck(envelope.getDeliveryTag(), false);
          }
        });
        ```
      
* 3.广播模式fanout
    * 引入交换机（一对多）一个生产者发送消息，多个消费者同时消费
    * 多个消费者，每个消费者有自己的queue，每个queue都要绑定到交换机
    * 生产：
    ```
    //参数一：交换机名称，参数二：交换机类型
    channel.exchangeDeclare("orders", "fanout");
    channel.basicPublish("orders", "", null, "fanout type message".getBytes());
    ```
    		
	* 消费：
	```
    channel.exchangeDeclare("orders", "fanout");
    //临时队列
    String queue = channel.queueDeclare().getQueue();
    channel.queueBind(queue, "orders", "");
    ```

* 4.路由模式Direct(直连)
	* 生产者生产消息，多个消费者根据routingKey选择消费消息
	* 生产：
	```
    channel.exchangeDeclare(exchangeName, "direct");
    channel.basicPublish(exchangeName, routingKey, null, ("这是direct基于routing_key[" + routingKey + "]发送的消息").getBytes());
    ```

    * 消费：
    ```
    channel.exchangeDeclare(exchangeName, "direct");
    //临时队列
    String queue = channel.queueDeclare().getQueue();
    channel.queueBind(queue, exchangeName, routingKey);
    channel.basicConsume(queue, true, new DefaultConsumer(channel) {});
    ```

* 5.动态路由模式Topic
	* routingKey支持通配符
	* \* 匹配单个单词
	* \# 匹配多个单词
    * 生产：
    ```
    String routingKey = "user.save.find";
    ```
  
	* 消费1：
	```
    String routingKey = "user.*";
    ```
 
	* 消费2：
    ```
    String routingKey = "user.#";
    ```

**MQ 应用场景**
* 1.异步处理
* 2.应用解耦
* 3.流量削峰

**集群搭建**
* 1.普通集群
    * 主备模式，主节点和生产者打交道，从节点同步主节点的数据，仅仅同步交换机的数据，队列的数据没有同步
    * 主要解决当集群中某一时刻master节点宕机，可以对queue中信息进行备份，没有自动的故障转移
    * 主节点活着，主节点和从节点都可以消费消息，主节点增加队列，从节点同步
    * 主节点宕机，从节点不能消费消息
* 2.镜像集群
    * 镜像队列机制就是将队列在三个节点之间设置主从关系，消息会在三个节点之间进行自动同步，且如果有一个节点不可用，并不会导致消息丢失或服务不可用的情况。
