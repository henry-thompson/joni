/*
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.joni.test;

import org.jcodings.Encoding;
import org.jcodings.specific.UTF8Encoding;
import org.joni.Option;
import org.joni.Syntax;
import org.joni.exception.ErrorMessages;

public class TestError extends Test {
    @Override
    public int option() {
        return Option.DEFAULT;
    }

    @Override
    public Encoding encoding() {
        return UTF8Encoding.INSTANCE;
    }

    @Override
    public String testEncoding() {
        return "iso-8859-2";
    }

    @Override
    public Syntax syntax() {
        return Syntax.TEST;
    }

    @Override
    public void test() throws Exception {
	    xerrs("(", ErrorMessages.ERR_END_PATTERN_WITH_UNMATCHED_PARENTHESIS);
	    xerrs("[[:WoRd:]]", ErrorMessages.ERR_INVALID_POSIX_BRACKET_TYPE);
	    xerrs("(0?0|(?(1)||)|(?(1)||))?", ErrorMessages.ERR_INVALID_CONDITION_PATTERN);
	    xerrs("[\\40000000000", ErrorMessages.ERR_TOO_BIG_NUMBER);
	    xerrs("[\\40000000000\n", ErrorMessages.ERR_TOO_BIG_NUMBER);
	    xerrs("[]", ErrorMessages.ERR_EMPTY_CHAR_CLASS);
	    xerrs("[c-a]", ErrorMessages.ERR_EMPTY_RANGE_IN_CHAR_CLASS);
	    xerrs("\\x{FFFFFFFF}", ErrorMessages.ERR_TOO_BIG_WIDE_CHAR_VALUE);
	    xerrs("\\x{100000000}", ErrorMessages.ERR_TOO_LONG_WIDE_CHAR_VALUE);
	    xerrs("\\u026x", ErrorMessages.ERR_TOO_SHORT_DIGITS);
	    xerrs("()(?\\!(?'a')\\1)", ErrorMessages.ERR_UNDEFINED_GROUP_OPTION);
	    xerrs("\\((", ErrorMessages.ERR_END_PATTERN_WITH_UNMATCHED_PARENTHESIS);
	    xerrs("(|", ErrorMessages.ERR_END_PATTERN_WITH_UNMATCHED_PARENTHESIS);
	    xerrs("'/g\\\u00ff\u00ff\u00ff\u00ff&))", ErrorMessages.ERR_UNMATCHED_CLOSE_PARENTHESIS);
	    xerrs("[0-0-\u00ff  ", ErrorMessages.ERR_PREMATURE_END_OF_CHAR_CLASS); // \xe2
	    xerrs("\\p{foobarbaz}", ErrorMessages.ERR_INVALID_CHAR_PROPERTY_NAME.replace("%n", "foobarbaz"));
	    //xerrs("\\p{あ}", ErrorMessages.ERR_INVALID_CHAR_PROPERTY_NAME.replace("%n", "あ"));

	    xerrs("a{100001}", ErrorMessages.ERR_TOO_BIG_NUMBER_FOR_REPEAT_RANGE);
	    xerrs("a{0,100001}", ErrorMessages.ERR_TOO_BIG_NUMBER_FOR_REPEAT_RANGE);
	    xerrs("a{5,1}", ErrorMessages.ERR_UPPER_SMALLER_THAN_LOWER_IN_REPEAT_RANGE);
	    xerrs("[\\6000", ErrorMessages.ERR_TOO_BIG_NUMBER); // CVE-2017-9226
	    xerrs("[\\H- ]", ErrorMessages.ERR_UNMATCHED_RANGE_SPECIFIER_IN_CHAR_CLASS); // CVE-2017-9228

	    xerrs("(?:ab|cd)*\\1", ErrorMessages.ERR_INVALID_BACKREF);

	    xerrs("(.(?=\\g<1>))", ErrorMessages.ERR_NEVER_ENDING_RECURSION);
	    xerrs("(a)(?<n>b)\\g<1>\\g<n>", ErrorMessages.ERR_NUMBERED_BACKREF_OR_CALL_NOT_ALLOWED);

	    // xerrs("(?<", ErrorMessages.ERR_END_PATTERN_WITH_UNMATCHED_PARENTHESIS);
	    xerrs("(?<>)", ErrorMessages.ERR_EMPTY_GROUP_NAME);
	    // xerrs("(?<.>)", ErrorMessages.ERR_INVALID_CHAR_IN_GROUP_NAME);
    }
}
