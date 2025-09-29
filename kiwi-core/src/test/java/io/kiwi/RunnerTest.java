package io.kiwi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RunnerTest {
    @Test
    public void testGetTags(){
        String[] args = {"--glue", "project.glue", "--tags", "tag1,tag2,tag3", "--plugin", "pretty", "path/to/features"};
        String tags = Runner.getTags(args);
        Assertions.assertEquals("tag1,tag2,tag3", tags);
    }
}
