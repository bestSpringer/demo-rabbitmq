package rabbit.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbit.utils.RabbitmqUtils;

import java.io.IOException;

public class TopicProvider {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("topics", "topic");
        String routingKey = "user.save.find";
        channel.basicPublish("topics", routingKey, null, ("这是topics模式routingKey为" + routingKey).getBytes());
        RabbitmqUtils.closeConnectionAndChannel(channel, connection);
    }
}
