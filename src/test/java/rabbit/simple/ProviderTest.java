package rabbit.simple;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProviderTest {

    @Test
    public void testProvider() throws IOException, TimeoutException {
        Provider provider = new Provider();
        provider.sendMsg();
    }
}
