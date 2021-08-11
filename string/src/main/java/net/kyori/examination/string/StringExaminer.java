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

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.kyori.examination.AbstractExaminer;
import net.kyori.examination.Examiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@link Examiner} that outputs a {@link String}.
 *
 * @since 1.0.0
 */
public class StringExaminer extends AbstractExaminer<String> {
  private static final Function<String, String> DEFAULT_ESCAPER = string -> string
    .replace("\"", "\\\"")
    .replace("\\", "\\\\")
    .replace("\b", "\\b")
    .replace("\f", "\\f")
    .replace("\n", "\\n")
    .replace("\r", "\\r")
    .replace("\t", "\\t");
  private static final Collector<CharSequence, ?, String> COMMA_CURLY = Collectors.joining(", ", "{", "}");
  private static final Collector<CharSequence, ?, String> COMMA_SQUARE = Collectors.joining(", ", "[", "]");
  private final Function<String, String> escaper;

  /**
   * Gets a string examiner that escapes simply.
   *
   * @return a string examiner
   * @since 1.0.0
   */
  public static @NotNull StringExaminer simpleEscaping() {
    return Instances.SIMPLE_ESCAPING;
  }

  /**
   * Constructs.
   *
   * @param escaper the string escaper
   * @since 1.0.0
   */
  public StringExaminer(final @NotNull Function<String, String> escaper) {
    this.escaper = escaper;
  }

  @Override
  protected <E> @NotNull String array(final E@NotNull[] array, final @NotNull Stream<String> elements) {
    return elements.collect(COMMA_SQUARE);
  }

  @Override
  protected <E> @NotNull String collection(final @NotNull Collection<E> collection, final @NotNull Stream<String> elements) {
    return elements.collect(COMMA_SQUARE);
  }

  @Override
  protected @NotNull String examinable(final @NotNull String name, final @NotNull Stream<Map.Entry<String, String>> properties) {
    return name + properties.map(property -> property.getKey() + '=' + property.getValue()).collect(COMMA_CURLY);
  }

  @Override
  protected <K, V> @NotNull String map(final @NotNull Map<K, V> map, final @NotNull Stream<Map.Entry<String, String>> entries) {
    return entries.map(entry -> entry.getKey() + '=' + entry.getValue()).collect(COMMA_CURLY);
  }

  @Override
  protected @NotNull String nil() {
    return "null";
  }

  @Override
  protected @NotNull String scalar(final @NotNull Object value) {
    return String.valueOf(value);
  }

  @Override
  public @NotNull String examine(final boolean value) {
    return String.valueOf(value);
  }

  @Override
  public @NotNull String examine(final byte value) {
    return String.valueOf(value);
  }

  @Override
  public @NotNull String examine(final char value) {
    return Strings.wrapIn(this.escaper.apply(String.valueOf(value)), '\'');
  }

  @Override
  public @NotNull String examine(final double value) {
    return Strings.withSuffix(String.valueOf(value), 'd');
  }

  @Override
  public @NotNull String examine(final float value) {
    return Strings.withSuffix(String.valueOf(value), 'f');
  }

  @Override
  public @NotNull String examine(final int value) {
    return String.valueOf(value);
  }

  @Override
  public @NotNull String examine(final long value) {
    return String.valueOf(value);
  }

  @Override
  public @NotNull String examine(final short value) {
    return String.valueOf(value);
  }

  @Override
  protected <T> @NotNull String stream(final @NotNull Stream<T> stream) {
    return stream.map(this::examine).collect(COMMA_SQUARE);
  }

  @Override
  protected @NotNull String stream(final @NotNull DoubleStream stream) {
    return stream.mapToObj(this::examine).collect(COMMA_SQUARE);
  }

  @Override
  protected @NotNull String stream(final @NotNull IntStream stream) {
    return stream.mapToObj(this::examine).collect(COMMA_SQUARE);
  }

  @Override
  protected @NotNull String stream(final @NotNull LongStream stream) {
    return stream.mapToObj(this::examine).collect(COMMA_SQUARE);
  }

  @Override
  public @NotNull String examine(final @Nullable String value) {
    if(value == null) return this.nil();
    return Strings.wrapIn(this.escaper.apply(value), '"');
  }

  @Override
  protected @NotNull String array(final int length, final IntFunction<String> value) {
    final StringBuilder sb = new StringBuilder();
    sb.append('[');
    for(int i = 0; i < length; i++) {
      sb.append(value.apply(i));
      if(i + 1 < length) {
        sb.append(", ");
      }
    }
    sb.append(']');
    return sb.toString();
  }

  private static final class Instances {
    static final StringExaminer SIMPLE_ESCAPING = new StringExaminer(DEFAULT_ESCAPER);
  }
}
