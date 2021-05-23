/*
 * This file is part of examination, licensed under the MIT License.
 *
 * Copyright (c) 2018-2021 KyoriPowered
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

import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.NonNull;

final class Strings {
  private Strings() {
  }

  static @NonNull String withSuffix(final String string, final char suffix) {
    return string + suffix;
  }

  static @NonNull String wrapIn(final String string, final char wrap) {
    return wrap + string + wrap;
  }

  static int maxLength(final Stream<String> strings) {
    return strings.mapToInt(String::length).max().orElse(0);
  }

  static @NonNull String repeat(final @NonNull String string, final int count) {
    if(count == 0) {
      return "";
    } else if(count == 1) {
      return string;
    }
    final StringBuilder sb = new StringBuilder(string.length() * count);
    for(int i = 0; i < count; ++i) {
      sb.append(string);
    }
    return sb.toString();
  }

  static @NonNull String padEnd(final @NonNull String string, final int minLength, final char padding) {
    return string.length() >= minLength
      ? string
      : String.format("%-" + minLength + "s", padding);
  }
}
