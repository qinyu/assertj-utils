package info.qinyu;

import info.qinyu.StringMapTestHelper;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.HashMap;

public class StringMapTestHelperTest {

    interface Message {
        Message hello(String value);
        Message world(String value);
    }

    interface AnotherMessage {
        AnotherMessage foo(String value);
        AnotherMessage bar(String value);
    }

    @Test
    public void should_assert_map_entry_with_proxy() throws Exception {
        HashMap<String, String> mapOfMessage = new HashMap<String, String>();
        StringMapTestHelper<String> helper = StringMapTestHelper.helper(mapOfMessage);
        helper.buildFor(Message.class).hello("some value").world(null);
        helper.assertThat(Message.class).hello("some value").world(null);

        // different message definitions work on same map
        helper.buildFor(AnotherMessage.class).foo("different value").bar(null);
        helper.assertThat(AnotherMessage.class).foo("different value").bar(null);

        // soft works ok
        SoftAssertions soft = new SoftAssertions();
        helper.assertThatSoftly(Message.class, soft).hello("some value").world(null);
        helper.assertThatSoftly(AnotherMessage.class, soft).foo("different value").bar(null);
        soft.assertAll();

        // helpers share same map
        StringMapTestHelper<String> anotherHelper = StringMapTestHelper.helper(mapOfMessage);
        anotherHelper.buildFor(Message.class).hello("some value").world("");
        anotherHelper.assertThat(Message.class).hello("some value").world("");
        helper.assertThat(Message.class).hello("some value").world("");
    }

}
