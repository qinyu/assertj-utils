package me.qinyu;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

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
        StringMapTestHelper<String> mapHelper = StringMapTestHelper.helper();
        mapHelper.buildFor(Message.class)
                .hello("some value")
                .world(null);
        mapHelper.assertThat(Message.class)
                .hello("some value")
                .world(null);

        // different message definitions work on same map
        mapHelper.buildFor(AnotherMessage.class)
                .foo("different value")
                .bar(null);
        mapHelper.assertThat(AnotherMessage.class)
                .foo("different value")
                .bar(null);

        // soft works ok
        SoftAssertions soft = new SoftAssertions();
        mapHelper.assertThatSoftly(Message.class, soft)
                .hello("some value")
                .world(null);
        mapHelper.assertThatSoftly(AnotherMessage.class, soft)
                .foo("different value")
                .bar(null);
        soft.assertAll();
    }

}
