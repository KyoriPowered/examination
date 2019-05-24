/*
 * This file is part of examination, licensed under the MIT License.
 *
 * Copyright (c) 2018-2019 KyoriPowered
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
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringExaminerTest {
  private final StringExaminer examiner = new StringExaminer();

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
  }

  @Test
  void testCollection_0() {
    assertEquals("[]", this.examiner.examine(Collections.emptyList()));
  }

  @Test
  void testCollection_1() {
    assertEquals("[\"abc\"]", this.examiner.examine(Collections.singleton("abc")));
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
    assertEquals("null", this.examiner.examine(null));
  }

  @Test
  void testScalar() {
    assertEquals("'a'", this.examiner.examine('a'));
    assertEquals("\"abc\"", this.examiner.examine("abc"));
    assertEquals("1", this.examiner.examine(1));
    assertEquals("1.23", this.examiner.examine(1.23d));
    assertEquals("1.23", this.examiner.examine(1.23f));
  }

  @Test
  void testStream_0() {
    assertEquals("[]", this.examiner.examine(Stream.empty()));
  }

  @Test
  void testStream_1() {
    assertEquals("[\"abc\"]", this.examiner.examine(Stream.of("abc")));
  }

  @Test
  void testStream_2() {
    assertEquals("[\"abc\", \"def\"]", this.examiner.examine(Stream.of("abc", "def")));
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
