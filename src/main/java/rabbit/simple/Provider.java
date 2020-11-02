package rabbit.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import rabbit.utils.RabbitmqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Provider {

    public void sendMsg() throws IOException, TimeoutException {
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("localhost");
//        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("/ems");
//        connectionFactory.setUsername("ems");
//        connectionFactory.setPassword("123");
//        Connection connection = connectionFactory.newConnection();

        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();

        //通道绑定消息队列
        //参数一：队列名称：hello为消息队列名字
        //参数二：durable：定义队列是否需要持久化，true表示持久化
        //参数三：exclusive是否为独占队列，true为独占队列（独占队列：表示当前队列只允许当前连接可用）
        //参数四：autoDelete：是否在消费完成后自动删除队列，true自动删除
        //参数五：附加参数
        channel.queueDeclare("hello", true, false, false, null);

        //发布消息：交换机名称，队列名称，额外设置，消息体
        //MessageProperties.PERSISTENT_TEXT_PLAIN 服务重启后消息依然存在
//        channel.basicPublish("", "hello", null, "hello wuba".getBytes());
        channel.basicPublish("", "hello", MessageProperties.PERSISTENT_TEXT_PLAIN, "hellowuba".getBytes());

//        channel.close();
//        connection.close();
        RabbitmqUtils.closeConnectionAndChannel(channel, connection);
    }

}
