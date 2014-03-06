package com.pw.common.json;

import com.pw.common.logging.LogWrapper;
import org.codehaus.jackson.SerializableString;
import org.codehaus.jackson.io.CharacterEscapes;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: carl
 * Date: 7/6/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomerCharacterEscapes extends CharacterEscapes {
//    Logger logger = LogWrapper.getLogger(CustomerCharacterEscapes.class).getWrappedLogger();
    private final int[] _asciiEscapes;

    public CustomerCharacterEscapes() {
        _asciiEscapes = standardAsciiEscapesForJSON();
        _asciiEscapes['\''] = CharacterEscapes.ESCAPE_STANDARD;
        _asciiEscapes['\"'] = CharacterEscapes.ESCAPE_STANDARD;
        _asciiEscapes['/'] = CharacterEscapes.ESCAPE_STANDARD;
        _asciiEscapes['<'] = CharacterEscapes.ESCAPE_STANDARD;
        _asciiEscapes['>'] = CharacterEscapes.ESCAPE_STANDARD;
        _asciiEscapes['\n'] = CharacterEscapes.ESCAPE_STANDARD;
        _asciiEscapes['\r'] = CharacterEscapes.ESCAPE_STANDARD;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return _asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(final int ch) {
        return new SerializableString(){
            @Override
            public String getValue() {
                return "\\"+_asciiEscapes[ch];
            }

            @Override
            public int charLength() {
                return 0;
            }

            @Override
            public char[] asQuotedChars() {
                return new char[0];
            }

            @Override
            public byte[] asUnquotedUTF8() {
                return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public byte[] asQuotedUTF8() {
                return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }
}
