/*
 * This file is part of examination, licensed under the MIT License.
 *
 * Copyright (c) 2018-2020 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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
package net.kyori.examination.string;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringExaminerTest {
  private final StringExaminer examiner = StringExaminer.simpleEscaping();

  @Test
  void testArray_0() {
    assertEquals("[]", this.examiner.examine(new String[]{}));
  }

  @Test
  void testArray_1() {
    assertEquals("[\"abc\"]", this.examiner.examine(new String[]{"abc"}));
  }

  @Test
  void testArray_2() {
    assertEquals("[\"abc\", \"def\"]", this.examiner.examine(new String[]{"abc", "def"}));
    assertEquals("[\"abc\", null]", this.examiner.examine(new String[]{"abc", null}));
  }

  @Test
  void testCollection_0() {
    assertEquals("[]", this.examiner.examine(Collections.emptyList()));
  }

  @Test
  void testCollection_1() {
    assertEquals("[\"abc\"]", this.examiner.examine(Collections.singleton("abc")));
    assertEquals("[null]", this.examiner.examine(Collections.singleton(null)));
  }

  @Test
  void testCollection_2() {
    assertEquals("[\"abc\", \"def\"]", this.examiner.examine(Arrays.asList("abc", "def")));
  }

  @Test
  void testExaminable() {
    assertEquals("ExaminableA{abc=\"def\", ghi=ExaminableC{jkl=\"mno\", pqr=\"stu\", vwx=\"yz\"}}", this.examiner.examine(new ExaminableA()));
    assertEquals("ExaminableA{abc=\"def\", ghi=ExaminableC{jkl=\"mno\", pqr=\"stu\", vwx=\"yz\"}}", new ExaminableA().examine(this.examiner));
  }

  @Test
  void testMap_0() {
    assertEquals("{}", this.examiner.examine(Collections.emptyMap()));
  }

  @Test
  void testMap_1() {
    assertEquals("{\"abc\"=\"def\"}", this.examiner.examine(ImmutableMap.of(
      "abc", "def"
    )));
  }

  @Test
  void testMap_2() {
    assertEquals("{\"abc\"=\"def\", \"ghi\"=\"jkl\"}", this.examiner.examine(ImmutableMap.of(
      "abc", "def",
      "ghi", "jkl"
    )));
  }

  @Test
  void testNil() {
    assertEquals("null", this.examiner.examine((Object) null));
  }

  @Test
  void testScalar() {
    final Object object = new Object();
    assertEquals(object.toString(), this.examiner.examine(object));
  }

  @Test
  void testScalar_redirect() {
    assertEquals("true", this.examiner.examine((Boolean) true));
    assertEquals("1", this.examiner.examine((Byte) (byte) 1));
    assertEquals("'a'", this.examiner.examine((Character) 'a'));
    assertEquals("1.2d", this.examiner.examine((Double) 1.2d));
    assertEquals("1.2f", this.examiner.examine((Float) 1.2f));
    assertEquals("1", this.examiner.examine((Integer) 1));
    assertEquals("1", this.examiner.examine((Long) 1L));
    assertEquals("1", this.examiner.examine((Short) (short) 1));

    assertEquals("[true]", this.examiner.examine((Object) new boolean[]{true}));
    assertEquals("[1]", this.examiner.examine((Object) new byte[]{(byte) 1}));
    assertEquals("['a']", this.examiner.examine((Object) new char[]{'a'}));
    assertEquals("[1.2d]", this.examiner.examine((Object) new double[]{1.2d}));
    assertEquals("[1.2f]", this.examiner.examine((Object) new float[]{1.2f}));
    assertEquals("[1]", this.examiner.examine((Object) new int[]{1}));
    assertEquals("[1]", this.examiner.examine((Object) new long[]{1L}));
    assertEquals("[1]", this.examiner.examine((Object) new short[]{(short) 1}));
  }

  @Test
  void testStream_0() {
    assertEquals("[]", this.examiner.examine(Stream.empty()));
    assertEquals("[]", this.examiner.examine(DoubleStream.empty()));
    assertEquals("[]", this.examiner.examine(IntStream.empty()));
    assertEquals("[]", this.examiner.examine(LongStream.empty()));
  }

  @Test
  void testStream_1() {
    assertEquals("[\"abc\"]", this.examiner.examine(Stream.of("abc")));
    assertEquals("[1.3d]", this.examiner.examine(DoubleStream.of(1.3d)));
    assertEquals("[1]", this.examiner.examine(IntStream.of(1)));
    assertEquals("[1]", this.examiner.examine(LongStream.of(1L)));
  }

  @Test
  void testStream_2() {
    assertEquals("[\"abc\", \"def\"]", this.examiner.examine(Stream.of("abc", "def")));
    assertEquals("[1.3d, 2.4d]", this.examiner.examine(DoubleStream.of(1.3d, 2.4d)));
    assertEquals("[1, 2]", this.examiner.examine(IntStream.of(1, 2)));
    assertEquals("[1, 2]", this.examiner.examine(LongStream.of(1L, 2L)));
  }

  @Test
  void testExamine_boolean() {
    assertEquals("true", this.examiner.examine(true));
  }

  @Test
  void testExamine_boolean_array_0() {
    assertEquals("[]", this.examiner.examine(new boolean[]{}));
  }

  @Test
  void testExamine_boolean_array_1() {
    assertEquals("[true]", this.examiner.examine(new boolean[]{true}));
  }

  @Test
  void testExamine_boolean_array_2() {
    assertEquals("[true, false]", this.examiner.examine(new boolean[]{true, false}));
  }

  @Test
  void testExamine_byte() {
    assertEquals("123", this.examiner.examine((byte) 123));
  }

  @Test
  void testExamine_byte_array_0() {
    assertEquals("[]", this.examiner.examine(new byte[]{}));
  }

  @Test
  void testExamine_byte_array_1() {
    assertEquals("[3]", this.examiner.examine(new byte[]{(byte) 3}));
  }

  @Test
  void testExamine_byte_array_2() {
    assertEquals("[1, 2]", this.examiner.examine(new byte[]{1, 2}));
  }

  @Test
  void testExamine_char() {
    assertEquals("'k'", this.examiner.examine('k'));
  }

  @Test
  void testExamine_char_array_0() {
    assertEquals("[]", this.examiner.examine(new char[]{}));
  }

  @Test
  void testExamine_char_array_1() {
    assertEquals("['a']", this.examiner.examine(new char[]{'a'}));
  }

  @Test
  void testExamine_char_array_2() {
    assertEquals("['a', 'b']", this.examiner.examine(new char[]{'a', 'b'}));
  }

  @Test
  void testExamine_double() {
    assertEquals("0.4d", this.examiner.examine(0.4d));
  }

  @Test
  void testExamine_double_array_0() {
    assertEquals("[]", this.examiner.examine(new double[]{}));
  }

  @Test
  void testExamine_double_array_1() {
    assertEquals("[1.0d]", this.examiner.examine(new double[]{1d}));
  }

  @Test
  void testExamine_double_array_2() {
    assertEquals("[1.2d, 2.3d]", this.examiner.examine(new double[]{1.2d, 2.3d}));
  }

  @Test
  void testExamine_float() {
    assertEquals("0.4f", this.examiner.examine(0.4f));
  }

  @Test
  void testExamine_float_array_0() {
    assertEquals("[]", this.examiner.examine(new float[]{}));
  }

  @Test
  void testExamine_float_array_1() {
    assertEquals("[1.0f]", this.examiner.examine(new float[]{1f}));
  }

  @Test
  void testExamine_float_array_2() {
    assertEquals("[1.2f, 2.3f]", this.examiner.examine(new float[]{1.2f, 2.3f}));
  }

  @Test
  void testExamine_int() {
    assertEquals("3", this.examiner.examine(3));
  }

  @Test
  void testExamine_int_array_0() {
    assertEquals("[]", this.examiner.examine(new int[]{}));
  }

  @Test
  void testExamine_int_array_1() {
    assertEquals("[1]", this.examiner.examine(new int[]{1}));
  }

  @Test
  void testExamine_int_array_2() {
    assertEquals("[1, 2]", this.examiner.examine(new int[]{1, 2}));
  }

  @Test
  void testExamine_long() {
    assertEquals("3", this.examiner.examine(3L));
  }

  @Test
  void testExamine_long_array_0() {
    assertEquals("[]", this.examiner.examine(new long[]{}));
  }

  @Test
  void testExamine_long_array_1() {
    assertEquals("[1]", this.examiner.examine(new long[]{1L}));
  }

  @Test
  void testExamine_long_array_2() {
    assertEquals("[1, 2]", this.examiner.examine(new long[]{1L, 2L}));
  }

  @Test
  void testExamine_short() {
    assertEquals("3", this.examiner.examine((short) 3));
  }

  @Test
  void testExamine_short_array_0() {
    assertEquals("[]", this.examiner.examine(new short[]{}));
  }

  @Test
  void testExamine_short_array_1() {
    assertEquals("[1]", this.examiner.examine(new short[]{(short) 1}));
  }

  @Test
  void testExamine_short_array_2() {
    assertEquals("[1, 2]", this.examiner.examine(new short[]{(short) 1, (short) 2}));
  }

  @Test
  void testString() {
    assertEquals("\"abc\"", this.examiner.examine("abc"));
  }

  private static class ExaminableA implements Examinable {
    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("abc", "def"),
        ExaminableProperty.of("ghi", new ExaminableC())
      );
    }
  }

  private static class ExaminableB implements Examinable {
    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("jkl", "mno"),
        ExaminableProperty.of("pqr", "stu")
      );
    }
  }

  private static class ExaminableC extends ExaminableB {
    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(
        super.examinableProperties(),
        Stream.of(
          ExaminableProperty.of("vwx", "yz")
        )
      );
    }
  }
}
