package com.company.qa.core.context;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class TestContextTest {

    @BeforeMethod
    public void setUp() {
        TestContext.reset();
    }

    @AfterMethod
    public void tearDown() {
        TestContext.reset();
    }

    @Test
    public void putAndGet_storeAndRetrieveValues() {
        TestContext.put("username", "testUser");
        String value = TestContext.get("username");
        assertThat(value).isEqualTo("testUser");
    }

    @Test
    public void putAndGet_withDifferentTypes() {
        TestContext.put("count", 42);
        TestContext.put("flag", true);
        TestContext.put("name", "test");

        Integer count = TestContext.get("count");
        Boolean flag = TestContext.get("flag");
        String name = TestContext.get("name");

        assertThat(count).isEqualTo(42);
        assertThat(flag).isTrue();
        assertThat(name).isEqualTo("test");
    }

    @Test
    public void get_withDefault_returnsDefaultWhenKeyMissing() {
        String value = TestContext.get("missing", "defaultValue");
        assertThat(value).isEqualTo("defaultValue");
    }

    @Test
    public void get_withDefault_returnsActualValueWhenKeyExists() {
        TestContext.put("key", "actualValue");
        String value = TestContext.get("key", "defaultValue");
        assertThat(value).isEqualTo("actualValue");
    }

    @Test
    public void containsKey_returnsTrueForExistingKey() {
        TestContext.put("existingKey", "value");
        assertThat(TestContext.containsKey("existingKey")).isTrue();
    }

    @Test
    public void containsKey_returnsFalseForMissingKey() {
        assertThat(TestContext.containsKey("missingKey")).isFalse();
    }

    @Test
    public void remove_removesAKey() {
        TestContext.put("toRemove", "value");
        assertThat(TestContext.containsKey("toRemove")).isTrue();

        TestContext.remove("toRemove");
        assertThat(TestContext.containsKey("toRemove")).isFalse();
        assertThat(TestContext.<String>get("toRemove")).isNull();
    }

    @Test
    public void clear_clearsAllEntries() {
        TestContext.put("key1", "value1");
        TestContext.put("key2", "value2");
        TestContext.put("key3", "value3");

        TestContext.clear();

        assertThat(TestContext.containsKey("key1")).isFalse();
        assertThat(TestContext.containsKey("key2")).isFalse();
        assertThat(TestContext.containsKey("key3")).isFalse();
    }

    @Test
    public void reset_removesThreadLocal() {
        TestContext.put("key", "value");
        TestContext.reset();

        // After reset, the ThreadLocal is removed; next get() creates a new empty map
        assertThat(TestContext.containsKey("key")).isFalse();
    }

    @Test
    public void instanceMethods_setValue_getValue_delegateCorrectly() {
        TestContext context = new TestContext();

        context.setValue("instanceKey", "instanceValue");
        String value = context.getValue("instanceKey");
        assertThat(value).isEqualTo("instanceValue");

        // Also accessible via static methods
        String staticValue = TestContext.get("instanceKey");
        assertThat(staticValue).isEqualTo("instanceValue");
    }

    @Test
    public void instanceMethods_getValueWithDefault_delegatesCorrectly() {
        TestContext context = new TestContext();
        String value = context.getValue("missingKey", "fallback");
        assertThat(value).isEqualTo("fallback");
    }

    @Test
    public void instanceMethods_hasKey_delegatesCorrectly() {
        TestContext context = new TestContext();
        context.setValue("testKey", "testValue");
        assertThat(context.hasKey("testKey")).isTrue();
        assertThat(context.hasKey("noKey")).isFalse();
    }

    @Test
    public void instanceMethods_removeValue_delegatesCorrectly() {
        TestContext context = new TestContext();
        context.setValue("removeMe", "value");
        assertThat(context.hasKey("removeMe")).isTrue();

        context.removeValue("removeMe");
        assertThat(context.hasKey("removeMe")).isFalse();
    }

    @Test
    public void instanceMethods_clearAll_delegatesCorrectly() {
        TestContext context = new TestContext();
        context.setValue("a", 1);
        context.setValue("b", 2);

        context.clearAll();

        assertThat(context.hasKey("a")).isFalse();
        assertThat(context.hasKey("b")).isFalse();
    }

    @Test
    public void instanceMethods_resetContext_delegatesCorrectly() {
        TestContext context = new TestContext();
        context.setValue("key", "val");

        context.resetContext();

        assertThat(context.hasKey("key")).isFalse();
    }

    @Test
    public void threadSafety_differentThreadsHaveIndependentContexts() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> threadValue = new AtomicReference<>();
        AtomicReference<Boolean> threadContainsMainKey = new AtomicReference<>();

        // Set value in main thread
        TestContext.put("mainKey", "mainValue");

        Thread otherThread = new Thread(() -> {
            try {
                // Check that main thread's key is NOT visible in this thread
                threadContainsMainKey.set(TestContext.containsKey("mainKey"));

                // Set a different value in this thread
                TestContext.put("threadKey", "threadValue");
                threadValue.set(TestContext.get("threadKey"));
            } finally {
                TestContext.reset();
                latch.countDown();
            }
        });

        otherThread.start();
        latch.await();

        // The other thread should NOT see main thread's key
        assertThat(threadContainsMainKey.get()).isFalse();

        // The other thread should have its own value
        assertThat(threadValue.get()).isEqualTo("threadValue");

        // Main thread should still have its own value
        assertThat(TestContext.<String>get("mainKey")).isEqualTo("mainValue");

        // Main thread should NOT see other thread's key
        assertThat(TestContext.containsKey("threadKey")).isFalse();
    }
}
