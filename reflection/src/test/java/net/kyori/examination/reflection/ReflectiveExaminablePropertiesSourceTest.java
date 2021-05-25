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
package net.kyori.examination.reflection;

import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

class ReflectiveExaminablePropertiesSourceTest {
  @Test
  void test() {
    final TestExaminable te = new TestExaminable("kashike", 0);
    te.examinableProperties().forEach(ep -> {
      System.out.println(ep.name() + ": " + ep.examine(StringExaminer.simpleEscaping()));
    });
  }

  static class TestExaminable implements Examinable {
    private final ReflectiveExaminableProperties reps = ReflectiveExaminableProperties.forFields(this);
    private final @Examine String name;
    private final @Examine int age;

    TestExaminable(final String name, final int age) {
      this.name = name;
      this.age = age;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return this.reps.examinableProperties();
    }
  }
}
