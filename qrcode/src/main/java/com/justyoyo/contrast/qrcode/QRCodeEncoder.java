/*
 * Copyright (C) 2008 ZXing authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justyoyo.contrast.qrcode;

import android.graphics.Bitmap;

import com.justyoyo.contrast.EncodeHintType;
import com.justyoyo.contrast.WriterException;
import com.justyoyo.contrast.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class does the work of decoding the user's request and extracting all
 * the data to be encoded in a barcode.
 *
 * @author Justin Wetherell (phishman3579@gmail.com )
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class QRCodeEncoder {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private int dimension = Integer.MIN_VALUE;
    private String contents = null;
    private String displayContents = null;
    private Map<EncodeHintType, Object> hints = null;
    private boolean encoded = false;

    public QRCodeEncoder(String data, int dimension, Map<EncodeHintType, Object> hints) {
        this.dimension = dimension;
        this.encoded = encodeContents(data);
        this.hints = hints;
    }

    public String getContents() {
        return contents;
    }

    public String getDisplayContents() {
        return displayContents;
    }


    public Bitmap encodeAsBitmap() throws WriterException {
        if (!encoded)
            return null;

        Map<EncodeHintType, Object> hints = this.hints;
        if (hints == null)
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);

        // Add encoding if it exits
        String encoding = (String) hints.get(EncodeHintType.CHARACTER_SET);
        if (encoding == null)
            encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix result = writer.encode(contents, dimension, dimension, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private boolean encodeContents(String data) {
        contents = data;
        displayContents = data;
        return contents != null && contents.length() > 0;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    private static String trim(String s) {
        if (s == null) {
            return null;
        }
        String result = s.trim();
        return result.length() == 0 ? null : result;
    }

    private static String escapeMECARD(String input) {
        if (input == null || (input.indexOf(':') < 0 && input.indexOf(';') < 0)) {
            return input;
        }
        int length = input.length();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (c == ':' || c == ';') {
                result.append('\\');
            }
            result.append(c);
        }
        return result.toString();
    }
}