package rabbit.fanout;

import com.rabbitmq.client.*;
import rabbit.utils.RabbitmqUtils;

import java.io.IOException;

public class FanCustomer2 {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("orders", "fanout");
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, "orders", "");
        channel.basicConsume(queue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者-2： " + new String(body));
            }
        });
    }
}
