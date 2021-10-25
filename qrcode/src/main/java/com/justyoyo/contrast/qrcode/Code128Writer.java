package com.justyoyo.contrast.qrcode;

import com.justyoyo.contrast.BarcodeFormat;
import com.justyoyo.contrast.EncodeHintType;
import com.justyoyo.contrast.OneDimensionalCodeWriter;
import com.justyoyo.contrast.WriterException;
import com.justyoyo.contrast.common.BitMatrix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by tiberiugolaes on 09/11/2016.
 */


public final class Code128Writer extends OneDimensionalCodeWriter {

    private static final char ESCAPE_FNC_1 = '\u00f1';
    private static final char ESCAPE_FNC_2 = '\u00f2';
    private static final char ESCAPE_FNC_3 = '\u00f3';
    private static final char ESCAPE_FNC_4 = '\u00f4';


    static final int[][] CODE_PATTERNS = {
            {2, 1, 2, 2, 2, 2}, // 0
            {2, 2, 2, 1, 2, 2},
            {2, 2, 2, 2, 2, 1},
            {1, 2, 1, 2, 2, 3},
            {1, 2, 1, 3, 2, 2},
            {1, 3, 1, 2, 2, 2}, // 5
            {1, 2, 2, 2, 1, 3},
            {1, 2, 2, 3, 1, 2},
            {1, 3, 2, 2, 1, 2},
            {2, 2, 1, 2, 1, 3},
            {2, 2, 1, 3, 1, 2}, // 10
            {2, 3, 1, 2, 1, 2},
            {1, 1, 2, 2, 3, 2},
            {1, 2, 2, 1, 3, 2},
            {1, 2, 2, 2, 3, 1},
            {1, 1, 3, 2, 2, 2}, // 15
            {1, 2, 3, 1, 2, 2},
            {1, 2, 3, 2, 2, 1},
            {2, 2, 3, 2, 1, 1},
            {2, 2, 1, 1, 3, 2},
            {2, 2, 1, 2, 3, 1}, // 20
            {2, 1, 3, 2, 1, 2},
            {2, 2, 3, 1, 1, 2},
            {3, 1, 2, 1, 3, 1},
            {3, 1, 1, 2, 2, 2},
            {3, 2, 1, 1, 2, 2}, // 25
            {3, 2, 1, 2, 2, 1},
            {3, 1, 2, 2, 1, 2},
            {3, 2, 2, 1, 1, 2},
            {3, 2, 2, 2, 1, 1},
            {2, 1, 2, 1, 2, 3}, // 30
            {2, 1, 2, 3, 2, 1},
            {2, 3, 2, 1, 2, 1},
            {1, 1, 1, 3, 2, 3},
            {1, 3, 1, 1, 2, 3},
            {1, 3, 1, 3, 2, 1}, // 35
            {1, 1, 2, 3, 1, 3},
            {1, 3, 2, 1, 1, 3},
            {1, 3, 2, 3, 1, 1},
            {2, 1, 1, 3, 1, 3},
            {2, 3, 1, 1, 1, 3}, // 40
            {2, 3, 1, 3, 1, 1},
            {1, 1, 2, 1, 3, 3},
            {1, 1, 2, 3, 3, 1},
            {1, 3, 2, 1, 3, 1},
            {1, 1, 3, 1, 2, 3}, // 45
            {1, 1, 3, 3, 2, 1},
            {1, 3, 3, 1, 2, 1},
            {3, 1, 3, 1, 2, 1},
            {2, 1, 1, 3, 3, 1},
            {2, 3, 1, 1, 3, 1}, // 50
            {2, 1, 3, 1, 1, 3},
            {2, 1, 3, 3, 1, 1},
            {2, 1, 3, 1, 3, 1},
            {3, 1, 1, 1, 2, 3},
            {3, 1, 1, 3, 2, 1}, // 55
            {3, 3, 1, 1, 2, 1},
            {3, 1, 2, 1, 1, 3},
            {3, 1, 2, 3, 1, 1},
            {3, 3, 2, 1, 1, 1},
            {3, 1, 4, 1, 1, 1}, // 60
            {2, 2, 1, 4, 1, 1},
            {4, 3, 1, 1, 1, 1},
            {1, 1, 1, 2, 2, 4},
            {1, 1, 1, 4, 2, 2},
            {1, 2, 1, 1, 2, 4}, // 65
            {1, 2, 1, 4, 2, 1},
            {1, 4, 1, 1, 2, 2},
            {1, 4, 1, 2, 2, 1},
            {1, 1, 2, 2, 1, 4},
            {1, 1, 2, 4, 1, 2}, // 70
            {1, 2, 2, 1, 1, 4},
            {1, 2, 2, 4, 1, 1},
            {1, 4, 2, 1, 1, 2},
            {1, 4, 2, 2, 1, 1},
            {2, 4, 1, 2, 1, 1}, // 75
            {2, 2, 1, 1, 1, 4},
            {4, 1, 3, 1, 1, 1},
            {2, 4, 1, 1, 1, 2},
            {1, 3, 4, 1, 1, 1},
            {1, 1, 1, 2, 4, 2}, // 80
            {1, 2, 1, 1, 4, 2},
            {1, 2, 1, 2, 4, 1},
            {1, 1, 4, 2, 1, 2},
            {1, 2, 4, 1, 1, 2},
            {1, 2, 4, 2, 1, 1}, // 85
            {4, 1, 1, 2, 1, 2},
            {4, 2, 1, 1, 1, 2},
            {4, 2, 1, 2, 1, 1},
            {2, 1, 2, 1, 4, 1},
            {2, 1, 4, 1, 2, 1}, // 90
            {4, 1, 2, 1, 2, 1},
            {1, 1, 1, 1, 4, 3},
            {1, 1, 1, 3, 4, 1},
            {1, 3, 1, 1, 4, 1},
            {1, 1, 4, 1, 1, 3}, // 95
            {1, 1, 4, 3, 1, 1},
            {4, 1, 1, 1, 1, 3},
            {4, 1, 1, 3, 1, 1},
            {1, 1, 3, 1, 4, 1},
            {1, 1, 4, 1, 3, 1}, // 100
            {3, 1, 1, 1, 4, 1},
            {4, 1, 1, 1, 3, 1},
            {2, 1, 1, 4, 1, 2},
            {2, 1, 1, 2, 1, 4},
            {2, 1, 1, 2, 3, 2}, // 105
            {2, 3, 3, 1, 1, 1, 2}
    };

    private static final float MAX_AVG_VARIANCE = 0.25f;
    private static final float MAX_INDIVIDUAL_VARIANCE = 0.7f;

    private static final int CODE_SHIFT = 98;

    private static final int CODE_CODE_C = 99;
    private static final int CODE_CODE_B = 100;
    private static final int CODE_CODE_A = 101;

    private static final int CODE_FNC_1 = 102;
    private static final int CODE_FNC_2 = 97;
    private static final int CODE_FNC_3 = 96;
    private static final int CODE_FNC_4_A = 101;
    private static final int CODE_FNC_4_B = 100;

    private static final int CODE_START_A = 103;
    private static final int CODE_START_B = 104;
    private static final int CODE_START_C = 105;
    private static final int CODE_STOP = 106;

    // Results of minimal lookahead for code C
    private enum CType {
        UNCODABLE,
        ONE_DIGIT,
        TWO_DIGITS,
        FNC_1
    }

    @Override
    public BitMatrix encode(String contents,
                            int width,
                            int height,
                            BarcodeFormat format,
                            Map<EncodeHintType, ?> hints) throws WriterException {
        if (format != BarcodeFormat.CODE_128) {
            throw new IllegalArgumentException("Can only encode CODE_128, but got " + format);
        }
        return super.encode(contents, width, height, format, hints);
    }

    @Override
    public boolean[] encode(String contents) {
        int length = contents.length();
        // Check length
        if (length < 1 || length > 80) {
            throw new IllegalArgumentException(
                    "Contents length should be between 1 and 80 characters, but got " + length);
        }
        // Check content
        for (int i = 0; i < length; i++) {
            char c = contents.charAt(i);
            if (c < ' ' || c > '~') {
                switch (c) {
                    case ESCAPE_FNC_1:
                    case ESCAPE_FNC_2:
                    case ESCAPE_FNC_3:
                    case ESCAPE_FNC_4:
                        break;
                    default:
                        throw new IllegalArgumentException("Bad character in input: " + c);
                }
            }
        }

        Collection<int[]> patterns = new ArrayList<>(); // temporary storage for patterns
        int checkSum = 0;
        int checkWeight = 1;
        int codeSet = 0; // selected code (CODE_CODE_B or CODE_CODE_C)
        int position = 0; // position in contents

        while (position < length) {
            //Select code to use
            int newCodeSet = chooseCode(contents, position, codeSet);

            //Get the pattern index
            int patternIndex;
            if (newCodeSet == codeSet) {
                // Encode the current character
                // First handle escapes
                switch (contents.charAt(position)) {
                    case ESCAPE_FNC_1:
                        patternIndex = CODE_FNC_1;
                        break;
                    case ESCAPE_FNC_2:
                        patternIndex = CODE_FNC_2;
                        break;
                    case ESCAPE_FNC_3:
                        patternIndex = CODE_FNC_3;
                        break;
                    case ESCAPE_FNC_4:
                        patternIndex = CODE_FNC_4_B; // FIXME if this ever outputs Code A
                        break;
                    default:
                        // Then handle normal characters otherwise
                        if (codeSet == CODE_CODE_B) {
                            patternIndex = contents.charAt(position) - ' ';
                        } else { // CODE_CODE_C
                            patternIndex = Integer.parseInt(contents.substring(position, position + 2));
                            position++; // Also incremented below
                        }
                }
                position++;
            } else {
                // Should we change the current code?
                // Do we have a code set?
                if (codeSet == 0) {
                    // No, we don't have a code set
                    if (newCodeSet == CODE_CODE_B) {
                        patternIndex = CODE_START_B;
                    } else {
                        // CODE_CODE_C
                        patternIndex = CODE_START_C;
                    }
                } else {
                    // Yes, we have a code set
                    patternIndex = newCodeSet;
                }
                codeSet = newCodeSet;
            }

            // Get the pattern
            patterns.add(CODE_PATTERNS[patternIndex]);

            // Compute checksum
            checkSum += patternIndex * checkWeight;
            if (position != 0) {
                checkWeight++;
            }
        }

        // Compute and append checksum
        checkSum %= 103;
        patterns.add(CODE_PATTERNS[checkSum]);

        // Append stop code
        patterns.add(CODE_PATTERNS[CODE_STOP]);

        // Compute code width
        int codeWidth = 0;
        for (int[] pattern : patterns) {
            for (int width : pattern) {
                codeWidth += width;
            }
        }

        // Compute result
        boolean[] result = new boolean[codeWidth];
        int pos = 0;
        for (int[] pattern : patterns) {
            pos += appendPattern(result, pos, pattern, true);
        }

        return result;
    }

    private static CType findCType(CharSequence value, int start) {
        int last = value.length();
        if (start >= last) {
            return CType.UNCODABLE;
        }
        char c = value.charAt(start);
        if (c == ESCAPE_FNC_1) {
            return CType.FNC_1;
        }
        if (c < '0' || c > '9') {
            return CType.UNCODABLE;
        }
        if (start + 1 >= last) {
            return CType.ONE_DIGIT;
        }
        c = value.charAt(start + 1);
        if (c < '0' || c > '9') {
            return CType.ONE_DIGIT;
        }
        return CType.TWO_DIGITS;
    }

    private static int chooseCode(CharSequence value, int start, int oldCode) {
        CType lookahead = findCType(value, start);
        if (lookahead == CType.UNCODABLE || lookahead == CType.ONE_DIGIT) {
            return CODE_CODE_B; // no choice
        }
        if (oldCode == CODE_CODE_C) { // can continue in code C
            return oldCode;
        }
        if (oldCode == CODE_CODE_B) {
            if (lookahead == CType.FNC_1) {
                return oldCode; // can continue in code B
            }
            // Seen two consecutive digits, see what follows
            lookahead = findCType(value, start + 2);
            if (lookahead == CType.UNCODABLE || lookahead == CType.ONE_DIGIT) {
                return oldCode; // not worth switching now
            }
            if (lookahead == CType.FNC_1) { // two digits, then FNC_1...
                lookahead = findCType(value, start + 3);
                if (lookahead == CType.TWO_DIGITS) { // then two more digits, switch
                    return CODE_CODE_C;
                } else {
                    return CODE_CODE_B; // otherwise not worth switching
                }
            }
            // At this point, there are at least 4 consecutive digits.
            // Look ahead to choose whether to switch now or on the next round.
            int index = start + 4;
            while ((lookahead = findCType(value, index)) == CType.TWO_DIGITS) {
                index += 2;
            }
            if (lookahead == CType.ONE_DIGIT) { // odd number of digits, switch later
                return CODE_CODE_B;
            }
            return CODE_CODE_C; // even number of digits, switch now
        }
        // Here oldCode == 0, which means we are choosing the initial code
        if (lookahead == CType.FNC_1) { // ignore FNC_1
            lookahead = findCType(value, start + 1);
        }
        if (lookahead == CType.TWO_DIGITS) { // at least two digits, start in code C
            return CODE_CODE_C;
        }
        return CODE_CODE_B;
    }

}