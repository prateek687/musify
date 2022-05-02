package com.plugsurfung.musify.api;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileReaderUtil {
    private static final Logger log = LoggerFactory.getLogger(FileReaderUtil.class);

    public static String responseAsStringFromFile(String fileName) {
        try {
            return IOUtils.toString(IOUtils.resourceToURL(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage());
            return "{}";
        }
    }
}
