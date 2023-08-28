/*
 * This file is part of examination, licensed under the MIT License.
 *
 * Copyright (c) 2018-2023 KyoriPowered
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

import static com.google.common.truth.Truth8.assertThat;

class MultiLineStringExaminerTest {
  private final MultiLineStringExaminer examiner = MultiLineStringExaminer.simpleEscaping();

  @Test
  void testArray_0() {
    assertThat(this.examiner.examine(new String[]{})).containsExactly("[]");
  }

  @Test
  void testArray_1() {
    assertThat(this.examiner.examine(new String[]{"abc"})).containsExactly("[", "    \"abc\"", "]");
  }

  @Test
  void testArray_2() {
    assertThat(this.examiner.examine(new String[]{"abc", "def"})).containsExactly("[", "    \"abc\",", "    \"def\"", "]");
    assertThat(this.examiner.examine(new String[]{"abc", null})).containsExactly("[", "    \"abc\",", "    null", "]");
  }

  @Test
  void testCollection_0() {
    assertThat(this.examiner.examine(Collections.emptyList())).containsExactly("[]");
  }

  @Test
  void testCollection_1() {
    assertThat(this.examiner.examine(Collections.singleton("abc"))).containsExactly("[", "    \"abc\"", "]");
    assertThat(this.examiner.examine(Collections.singleton(null))).containsExactly("[", "    null", "]");
  }

  @Test
  void testCollection_2() {
    assertThat(this.examiner.examine(Arrays.asList("abc", "def"))).containsExactly("[", "    \"abc\",", "    \"def\"", "]");
  }

  @Test
  void testExaminable() {
    assertThat(this.examiner.examine(new ExaminableA())).containsExactly(
      "ExaminableA{",
      "    \"abc\" = \"def\",",
      "    \"ghi\" = ExaminableC{",
      "        \"jkl\" = \"mno\",",
      "        \"pqr\" = \"stu\",",
      "        \"vwx\" = \"yz\"",
      "    }",
      "}"
    );
    assertThat(new ExaminableA().examine(this.examiner)).containsExactly(
      "ExaminableA{",
      "    \"abc\" = \"def\",",
      "    \"ghi\" = ExaminableC{",
      "        \"jkl\" = \"mno\",",
      "        \"pqr\" = \"stu\",",
      "        \"vwx\" = \"yz\"",
      "    }",
      "}"
    );

    new ExaminableA().examine(MultiLineStringExaminer.simpleEscaping()).forEach(System.out::println);
  }

  @Test
  void testMap_0() {
    assertThat(this.examiner.examine(Collections.emptyMap())).containsExactly("{}");
  }

  @Test
  void testMap_1() {
    assertThat(this.examiner.examine(ImmutableMap.of(
      "abc", "def"
    ))).containsExactly(
      "{",
      "    \"abc\" = \"def\"",
      "}"
    );
  }

  @Test
  void testMap_2() {
    assertThat(this.examiner.examine(ImmutableMap.of(
      "abc", "def",
      "ghi", "jkl"
    ))).containsExactly(
      "{",
      "    \"abc\" = \"def\",",
      "    \"ghi\" = \"jkl\"",
      "}"
    );
  }

  @Test
  void testNil() {
    assertThat(this.examiner.examine((Object) null)).containsExactly("null");
  }

  @Test
  void testScalar() {
    final Object object = new Object();
    assertThat(this.examiner.examine(object)).containsExactly(object.toString());
  }

  @Test
  void testStream_0() {
    assertThat(this.examiner.examine(Stream.empty())).containsExactly("[]");
    assertThat(this.examiner.examine(DoubleStream.empty())).containsExactly("[]");
    assertThat(this.examiner.examine(IntStream.empty())).containsExactly("[]");
    assertThat(this.examiner.examine(LongStream.empty())).containsExactly("[]");
  }

  @Test
  void testStream_1() {
    assertThat(this.examiner.examine(Stream.of("abc"))).containsExactly("[", "    \"abc\"", "]");
    assertThat(this.examiner.examine(DoubleStream.of(1.3d))).containsExactly("[", "    1.3d", "]");
    assertThat(this.examiner.examine(IntStream.of(1))).containsExactly("[", "    1", "]");
    assertThat(this.examiner.examine(LongStream.of(1L))).containsExactly("[", "    1", "]");
  }

  @Test
  void testStream_2() {
    assertThat(this.examiner.examine(Stream.of("abc", "def"))).containsExactly("[", "    \"abc\",", "    \"def\"", "]");
    assertThat(this.examiner.examine(DoubleStream.of(1.3d, 2.4d))).containsExactly("[", "    1.3d,", "    2.4d", "]");
    assertThat(this.examiner.examine(IntStream.of(1, 2))).containsExactly("[", "    1,", "    2", "]");
    assertThat(this.examiner.examine(LongStream.of(1L, 2L))).containsExactly("[", "    1,", "    2", "]");
  }

  @Test
  void testExamine_boolean() {
    assertThat(this.examiner.examine(true)).containsExactly("true");
  }

  @Test
  void testExamine_boolean_array_0() {
    assertThat(this.examiner.examine(new boolean[]{})).containsExactly("[]");
  }

  @Test
  void testExamine_boolean_array_1() {
    assertThat(this.examiner.examine(new boolean[]{true})).containsExactly("[", "    true", "]");
  }

  @Test
  void testExamine_boolean_array_2() {
    assertThat(this.examiner.examine(new boolean[]{true, false})).containsExactly("[", "    true,", "    false", "]");
  }

  @Test
  void testExamine_byte() {
    assertThat(this.examiner.examine((byte) 123)).containsExactly("123");
  }

  @Test
  void testExamine_byte_array_0() {
    assertThat(this.examiner.examine(new byte[]{})).containsExactly("[]");
  }

  @Test
  void testExamine_byte_array_1() {
    assertThat(this.examiner.examine(new byte[]{(byte) 3})).containsExactly("[", "    3", "]");
  }

  @Test
  void testExamine_byte_array_2() {
    assertThat(this.examiner.examine(new byte[]{(byte) 1, (byte) 2})).containsExactly("[", "    1,", "    2", "]");
  }

  @Test
  void testExamine_char() {
    assertThat(this.examiner.examine('k')).containsExactly("'k'");
  }

  @Test
  void testExamine_char_array_0() {
    assertThat(this.examiner.examine(new char[]{})).containsExactly("[]");
  }

  @Test
  void testExamine_char_array_1() {
    assertThat(this.examiner.examine(new char[]{'a'})).containsExactly("[", "    'a'", "]");
  }

  @Test
  void testExamine_char_array_2() {
    assertThat(this.examiner.examine(new char[]{'a', 'b'})).containsExactly("[", "    'a',", "    'b'", "]");
  }

  @Test
  void testExamine_double() {
    assertThat(this.examiner.examine(0.4d)).containsExactly("0.4d");
  }

  @Test
  void testExamine_double_array_0() {
    assertThat(this.examiner.examine(new double[]{})).containsExactly("[]");
  }

  @Test
  void testExamine_double_array_1() {
    assertThat(this.examiner.examine(new double[]{1d})).containsExactly("[", "    1.0d", "]");
  }

  @Test
  void testExamine_double_array_2() {
    assertThat(this.examiner.examine(new double[]{1.2d, 2.3d})).containsExactly("[", "    1.2d,", "    2.3d", "]");
  }

  @Test
  void testExamine_float() {
    assertThat(this.examiner.examine(0.4f)).containsExactly("0.4f");
  }

  @Test
  void testExamine_float_array_0() {
    assertThat(this.examiner.examine(new float[]{})).containsExactly("[]");
  }

  @Test
  void testExamine_float_array_1() {
    assertThat(this.examiner.examine(new float[]{1f})).containsExactly("[", "    1.0f", "]");
  }

  @Test
  void testExamine_float_array_2() {
    assertThat(this.examiner.examine(new float[]{1.2f, 2.3f})).containsExactly("[", "    1.2f,", "    2.3f", "]");
  }

  @Test
  void testExamine_int() {
    assertThat(this.examiner.examine(3)).containsExactly("3");
  }

  @Test
  void testExamine_int_array_0() {
    assertThat(this.examiner.examine(new int[]{})).containsExactly("[]");
  }

  @Test
  void testExamine_int_array_1() {
    assertThat(this.examiner.examine(new int[]{1})).containsExactly("[", "    1", "]");
  }

  @Test
  void testExamine_int_array_2() {
    assertThat(this.examiner.examine(new int[]{1, 2})).containsExactly("[", "    1,", "    2", "]");
  }

  @Test
  void testExamine_long() {
    assertThat(this.examiner.examine(3L)).containsExactly("3");
  }

  @Test
  void testExamine_long_array_0() {
    assertThat(this.examiner.examine(new long[]{})).containsExactly("[]");
  }

  @Test
  void testExamine_long_array_1() {
    assertThat(this.examiner.examine(new long[]{1L})).containsExactly("[", "    1", "]");
  }

  @Test
  void testExamine_long_array_2() {
    assertThat(this.examiner.examine(new long[]{1L, 2L})).containsExactly("[", "    1,", "    2", "]");
  }

  @Test
  void testExamine_short() {
    assertThat(this.examiner.examine((short) 3)).containsExactly("3");
  }

  @Test
  void testExamine_short_array_0() {
    assertThat(this.examiner.examine(new short[]{})).containsExactly("[]");
  }

  @Test
  void testExamine_short_array_1() {
    assertThat(this.examiner.examine(new short[]{(short) 1})).containsExactly("[", "    1", "]");
  }

  @Test
  void testExamine_short_array_2() {
    assertThat(this.examiner.examine(new short[]{(short) 1, (short) 2})).containsExactly("[", "    1,", "    2", "]");
  }

  @Test
  void testString() {
    assertThat(this.examiner.examine("abc")).containsExactly("\"abc\"");
  }

  private static final class ExaminableA implements Examinable {
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

  private static final class ExaminableC extends ExaminableB {
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
