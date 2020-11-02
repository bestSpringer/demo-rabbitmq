package rabbit.task;

import org.junit.Test;

import java.io.IOException;

public class TaskProviderTest {

    @Test
    public void testSend() throws IOException {
        TaskProvider taskProvider = new TaskProvider();
        taskProvider.sendMsg();
    }
}
