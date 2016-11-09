package com.justyoyo.contrast.code128;

import android.graphics.Bitmap;

import com.justyoyo.contrast.BarcodeFormat;
import com.justyoyo.contrast.EncodeHintType;
import com.justyoyo.contrast.WriterException;
import com.justyoyo.contrast.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by tiberiugolaes on 09/11/2016.
 */

public final class Code128Encoder {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private int dimension = Integer.MIN_VALUE;
    private String displayContents = null;
    private String contents = null;
    private Map<EncodeHintType, Object> hints = null;
    private boolean encoded = false;

    public Code128Encoder(int dimension, Map<EncodeHintType, Object> hints) {
        this(null, dimension, hints);
    }

    public Code128Encoder(String data, int dimension, Map<EncodeHintType, Object> hints) {
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

    private static int getColor(Map<EncodeHintType, Object> hints, EncodeHintType key, int defaultColor) {
        Object foreground = hints.get(key);
        if (foreground == null || !(foreground instanceof Integer)) {
            return defaultColor;
        } else {
            return ((Integer) foreground);
        }
    }

    public void setData(String data) {
        encoded = encodeContents(data);
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
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
        Code128Writer writer = new Code128Writer();
        BitMatrix result = writer.encode(contents, dimension, dimension, BarcodeFormat.CODE_128, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];

        int foregroundColor = getColor(hints, EncodeHintType.FOREGROUND_COLOR, BLACK);
        int backgroundColor = getColor(hints, EncodeHintType.BACKGROUND_COLOR, WHITE);

        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? foregroundColor : backgroundColor;
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
}