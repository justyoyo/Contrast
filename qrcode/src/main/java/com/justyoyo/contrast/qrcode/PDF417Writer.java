package com.justyoyo.contrast.qrcode;

import com.justyoyo.contrast.BarcodeFormat;
import com.justyoyo.contrast.EncodeHintType;
import com.justyoyo.contrast.Writer;
import com.justyoyo.contrast.WriterException;
import com.justyoyo.contrast.common.BitMatrix;
import com.justyoyo.contrast.common.Compaction;
import com.justyoyo.contrast.common.Dimensions;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by tiberiugolaes on 08/11/2016.
 */

public final class PDF417Writer implements Writer {

    /**
     * default white space (margin) around the code
     */
    static final int WHITE_SPACE = 30;

    /**
     * default error correction level
     */
    static final int DEFAULT_ERROR_CORRECTION_LEVEL = 2;

    @Override
    public BitMatrix encode(String contents,
                            int width,
                            int height,
                            BarcodeFormat format,
                            Map<EncodeHintType, ?> hints) throws WriterException {
        if (format != BarcodeFormat.PDF_417) {
            throw new IllegalArgumentException("Can only encode PDF_417, but got " + format);
        }

        PDF417 encoder = new PDF417();
        int margin = WHITE_SPACE;
        int errorCorrectionLevel = DEFAULT_ERROR_CORRECTION_LEVEL;

        if (hints != null) {
            if (hints.containsKey(EncodeHintType.PDF417_COMPACT)) {
                encoder.setCompact(Boolean.valueOf(hints.get(EncodeHintType.PDF417_COMPACT).toString()));
            }
            if (hints.containsKey(EncodeHintType.PDF417_COMPACTION)) {
                encoder.setCompaction(Compaction.valueOf(hints.get(EncodeHintType.PDF417_COMPACTION).toString()));
            }
            if (hints.containsKey(EncodeHintType.PDF417_DIMENSIONS)) {
                Dimensions dimensions = (Dimensions) hints.get(EncodeHintType.PDF417_DIMENSIONS);
                encoder.setDimensions(dimensions.getMaxCols(),
                        dimensions.getMinCols(),
                        dimensions.getMaxRows(),
                        dimensions.getMinRows());
            }
            if (hints.containsKey(EncodeHintType.MARGIN)) {
                margin = Integer.parseInt(hints.get(EncodeHintType.MARGIN).toString());
            }
            if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = Integer.parseInt(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
            }
            if (hints.containsKey(EncodeHintType.CHARACTER_SET)) {
                Charset encoding = Charset.forName(hints.get(EncodeHintType.CHARACTER_SET).toString());
                encoder.setEncoding(encoding);
            }
        }

        return bitMatrixFromEncoder(encoder, contents, errorCorrectionLevel, width, height, margin);
    }

    @Override
    public BitMatrix encode(String contents,
                            BarcodeFormat format,
                            int width,
                            int height) throws WriterException {
        return encode(contents, width, height, format, null);
    }

    /**
     * Takes encoder, accounts for width/height, and retrieves bit matrix
     */

    private static BitMatrix bitMatrixFromEncoder(PDF417 encoder,
                                                  String contents,
                                                  int errorCorrectionLevel,
                                                  int width,
                                                  int height,
                                                  int margin) throws WriterException {
        encoder.generateBarcodeLogic(contents, errorCorrectionLevel);

        int aspectRatio = 4;
        byte[][] originalScale = encoder.getBarcodeMatrix().getScaledMatrix(1, aspectRatio);
        boolean rotated = false;
        if ((height > width) ^ (originalScale[0].length < originalScale.length)) {
            originalScale = rotateArray(originalScale);
            rotated = true;
        }

        int scaleX = width / originalScale[0].length;
        int scaleY = height / originalScale.length;

        int scale;
        if (scaleX < scaleY) {
            scale = scaleX;
        } else {
            scale = scaleY;
        }

        if (scale > 1) {
            byte[][] scaledMatrix =
                    encoder.getBarcodeMatrix().getScaledMatrix(scale, scale * aspectRatio);
            if (rotated) {
                scaledMatrix = rotateArray(scaledMatrix);
            }
            return bitMatrixFrombitArray(scaledMatrix, margin);
        }
        return bitMatrixFrombitArray(originalScale, margin);
    }

    /**
     * This takes an array holding the values of the PDF 417
     *
     * @param input  a byte array of information with 0 is black, and 1 is white
     * @param margin border around the barcode
     * @return BitMatrix of the input
     */
    private static BitMatrix bitMatrixFrombitArray(byte[][] input, int margin) {
        // Creates the bitmatrix with extra space for whitespace
        BitMatrix output = new BitMatrix(input[0].length + 2 * margin, input.length + 2 * margin);
        output.clear();
        for (int y = 0, yOutput = output.getHeight() - margin - 1; y < input.length; y++, yOutput--) {
            for (int x = 0; x < input[0].length; x++) {
                // Zero is white in the bytematrix
                if (input[y][x] == 1) {
                    output.set(x + margin, yOutput);
                }
            }
        }
        return output;
    }

    /**
     * Takes and rotates the it 90 degrees
     */
    private static byte[][] rotateArray(byte[][] bitarray) {
        byte[][] temp = new byte[bitarray[0].length][bitarray.length];
        for (int ii = 0; ii < bitarray.length; ii++) {
            // This makes the direction consistent on screen when rotating the
            // screen;
            int inverseii = bitarray.length - ii - 1;
            for (int jj = 0; jj < bitarray[0].length; jj++) {
                temp[jj][inverseii] = bitarray[ii][jj];
            }
        }
        return temp;
    }

}