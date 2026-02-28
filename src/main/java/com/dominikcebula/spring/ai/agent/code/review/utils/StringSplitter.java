package com.dominikcebula.spring.ai.agent.code.review.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

public class StringSplitter {
    public static List<String> splitByBytes(String input, int maxBytes, Charset charset) {
        List<String> result = new ArrayList<>();
        CharsetEncoder encoder = charset.newEncoder();

        int start = 0;

        while (start < input.length()) {
            int end = start + 1;
            int lastGoodEnd = start;

            while (end <= input.length()) {
                String substring = input.substring(start, end);
                try {
                    ByteBuffer bb = encoder.encode(CharBuffer.wrap(substring));
                    if (bb.remaining() <= maxBytes) {
                        lastGoodEnd = end;
                        end++;
                    } else {
                        break;
                    }
                } catch (CharacterCodingException e) {
                    break;
                }
            }

            if (lastGoodEnd == start) {
                throw new IllegalArgumentException("Single character exceeds max byte size");
            }

            result.add(input.substring(start, lastGoodEnd));
            start = lastGoodEnd;
        }

        return result;
    }
}
