package com.justyoyo.contrast;

import com.justyoyo.contrast.BarcodeFormat;
import com.justyoyo.contrast.EncodeHintType;
import com.justyoyo.contrast.Writer;
import com.justyoyo.contrast.WriterException;
import com.justyoyo.contrast.common.BitMatrix;

import java.util.Map;

/**
 * Created by tiberiugolaes on 08/11/2016.
 */

public abstract class OneDimensionalCodeWriter implements Writer {

    @Override
    public final BitMatrix encode(String contents, BarcodeFormat format, int width, int height)
            throws WriterException {
        return encode(contents, width, height, format, null);
    }

    /**
     * Encode the contents following specified format.
     * {@code width} and {@code height} are required size. This method may return bigger size
     * {@code BitMatrix} when specified size is too small. The user can set both {@code width} and
     * {@code height} to zero to get minimum size barcode. If negative value is set to {@code width}
     * or {@code height}, {@code IllegalArgumentException} is thrown.
     */
    @Override
    public BitMatrix encode(String contents,
                            int width,
                            int height,
                            BarcodeFormat format,
                            Map<EncodeHintType, ?> hints) throws WriterException {
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        }

        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Negative size is not allowed. Input: "
                    + width + 'x' + height);
        }

        int sidesMargin = getDefaultMargin();
        if (hints != null && hints.containsKey(EncodeHintType.MARGIN)) {
            sidesMargin = Integer.parseInt(hints.get(EncodeHintType.MARGIN).toString());
        }

        boolean[] code = encode(contents);
        return renderResult(code, width, height, sidesMargin);
    }

    /**
     * @return a byte array of horizontal pixels (0 = white, 1 = black)
     */
    private static BitMatrix renderResult(boolean[] code, int width, int height, int sidesMargin) {
        int inputWidth = code.length;
        // Add quiet zone on both sides.
        int fullWidth = inputWidth + sidesMargin;
        int outputWidth = Math.max(width, fullWidth);
        int outputHeight = Math.max(1, height);

        int multiple = outputWidth / fullWidth;
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;

        BitMatrix output = new BitMatrix(outputWidth, outputHeight);
        for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
            if (code[inputX]) {
                output.setRegion(outputX, 0, multiple, outputHeight);
            }
        }
        return output;
    }


    /**
     * @param target     encode black/white pattern into this array
     * @param pos        position to start encoding at in {@code target}
     * @param pattern    lengths of black/white runs to encode
     * @param startColor starting color - false for white, true for black
     * @return the number of elements added to target.
     */
    protected static int appendPattern(boolean[] target, int pos, int[] pattern, boolean startColor) {
        boolean color = startColor;
        int numAdded = 0;
        for (int len : pattern) {
            for (int j = 0; j < len; j++) {
                target[pos++] = color;
            }
            numAdded += len;
            color = !color; // flip color after each segment
        }
        return numAdded;
    }

    public int getDefaultMargin() {
        // CodaBar spec requires a side margin to be more than ten times wider than narrow space.
        // This seems like a decent idea for a default for all formats.
        return 10;
    }

    /**
     * Encode the contents to boolean array expression of one-dimensional barcode.
     * Start code and end code should be included in result, and side margins should not be included.
     *
     * @param contents barcode contents to encode
     * @return a {@code boolean[]} of horizontal pixels (false = white, true = black)
     */
    public abstract boolean[] encode(String contents);
}