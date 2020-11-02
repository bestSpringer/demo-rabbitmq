package rabbit.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbit.utils.RabbitmqUtils;

import java.io.IOException;

public class FanoutProvider {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        //参数一：交换机名称，参数二：交换机类型
        channel.exchangeDeclare("orders", "fanout");
        channel.basicPublish("orders", "", null, "fanout type message".getBytes());
        RabbitmqUtils.closeConnectionAndChannel(channel, connection);
    }

}
