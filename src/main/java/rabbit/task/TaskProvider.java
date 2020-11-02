package rabbit.task;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbit.utils.RabbitmqUtils;

import java.io.IOException;

public class TaskProvider {

    public void sendMsg() throws IOException {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("work", true, false, false, null);

        for (int i = 0; i <= 20; i++) {
            channel.basicPublish("", "work", null, (i + " hello work queue").getBytes());
        }
        RabbitmqUtils.closeConnectionAndChannel(channel, connection);
    }
}
