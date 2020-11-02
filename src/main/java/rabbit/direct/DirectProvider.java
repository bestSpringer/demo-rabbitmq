package rabbit.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbit.utils.RabbitmqUtils;

import java.io.IOException;

public class DirectProvider {

    public static void main(String[] args) throws IOException {
        String exchangeName = "logs_direct";
        String routingKey = "info";
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, "direct");
        channel.basicPublish(exchangeName, routingKey, null, ("这是direct基于routing_key[" + routingKey + "]发送的消息").getBytes());
        RabbitmqUtils.closeConnectionAndChannel(channel, connection);
    }
}
