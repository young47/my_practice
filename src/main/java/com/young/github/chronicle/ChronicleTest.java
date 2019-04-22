package com.young.github.chronicle;

import java.io.IOException;

public class ChronicleTest {

    public static void main(String[] args) throws IOException {
       /* String basePath = System.getProperty("java.io.tmpdir") + "/getting-started"
        ChronicleQueue queue = SingleChronicleQueueBuilder.single("queue-dir").build();

        //write
        ExcerptAppender appender = queue.acquireAppender();
        appender.writeDocument(w -> w.write(() -> "msg").text("TestMessage"));
        appender.writeText("TestMessage");

        //read
        ExcerptTailer tailer = queue.createTailer();

        tailer.readDocument(w -> System.out.println("msg: " + w.read(()->"msg").text()));

        assertEquals("TestMessage", tailer.readText());*/
    }
}
