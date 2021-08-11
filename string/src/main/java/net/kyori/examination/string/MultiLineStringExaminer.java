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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.kyori.examination.AbstractExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Thanks goes to the people at @OvercastNetwork
 */

/**
 * An examiner which produces a multi-line {@code String} as the output.
 *
 * @since 1.2.0
 */
public class MultiLineStringExaminer extends AbstractExaminer<Stream<String>> {
  private static final String INDENT_2 = "  ";
  private final StringExaminer examiner;

  /**
   * Gets a multi-line string examiner that escapes simply.
   *
   * @return a string examiner
   * @since 1.2.0
   */
  public static @NotNull MultiLineStringExaminer simpleEscaping() {
    return Instances.SIMPLE_ESCAPING;
  }

  /**
   * Constructs.
   *
   * @param examiner a single-line string examiner
   * @since 1.2.0
   */
  public MultiLineStringExaminer(final @NotNull StringExaminer examiner) {
    this.examiner = examiner;
  }

  @Override
  protected <E> @NotNull Stream<String> array(final E@NotNull[] array, final @NotNull Stream<Stream<String>> elements) {
    return this.arrayLike(elements);
  }

  @Override
  protected <E> @NotNull Stream<String> collection(final @NotNull Collection<E> collection, final @NotNull Stream<Stream<String>> elements) {
    return this.arrayLike(elements);
  }

  @Override
  protected @NotNull Stream<String> examinable(final @NotNull String name, final @NotNull Stream<Map.Entry<String, Stream<String>>> properties) {
    final Stream<String> flattened = flatten(",", properties.map(entry -> association(this.examine(entry.getKey()), " = ", entry.getValue())));
    final Stream<String> indented = indent(flattened);
    return enclose(indented, name + "{", "}");
  }

  @Override
  protected <K, V> @NotNull Stream<String> map(final @NotNull Map<K, V> map, final @NotNull Stream<Map.Entry<Stream<String>, Stream<String>>> entries) {
    final Stream<String> flattened = flatten(",", entries.map(entry -> association(entry.getKey(), " = ", entry.getValue())));
    final Stream<String> indented = indent(flattened);
    return enclose(indented, "{", "}");
  }

  @Override
  protected @NotNull Stream<String> nil() {
    return Stream.of(this.examiner.nil());
  }

  @Override
  protected @NotNull Stream<String> scalar(final @NotNull Object value) {
    return Stream.of(this.examiner.scalar(value));
  }

  @Override
  public @NotNull Stream<String> examine(final boolean value) {
    return Stream.of(this.examiner.examine(value));
  }

  @Override
  public @NotNull Stream<String> examine(final byte value) {
    return Stream.of(this.examiner.examine(value));
  }

  @Override
  public @NotNull Stream<String> examine(final char value) {
    return Stream.of(this.examiner.examine(value));
  }

  @Override
  public @NotNull Stream<String> examine(final double value) {
    return Stream.of(this.examiner.examine(value));
  }

  @Override
  public @NotNull Stream<String> examine(final float value) {
    return Stream.of(this.examiner.examine(value));
  }

  @Override
  public @NotNull Stream<String> examine(final int value) {
    return Stream.of(this.examiner.examine(value));
  }

  @Override
  public @NotNull Stream<String> examine(final long value) {
    return Stream.of(this.examiner.examine(value));
  }

  @Override
  public @NotNull Stream<String> examine(final short value) {
    return Stream.of(this.examiner.examine(value));
  }

  @Override
  protected @NotNull Stream<String> array(final int length, final IntFunction<Stream<String>> value) {
    return this.arrayLike(
      length == 0
        ? Stream.empty()
        : IntStream.range(0, length).mapToObj(value)
    );
  }

  @Override
  protected <T> @NotNull Stream<String> stream(final @NotNull Stream<T> stream) {
    return this.arrayLike(stream.map(this::examine));
  }

  @Override
  protected @NotNull Stream<String> stream(final @NotNull DoubleStream stream) {
    return this.arrayLike(stream.mapToObj(this::examine));
  }

  @Override
  protected @NotNull Stream<String> stream(final @NotNull IntStream stream) {
    return this.arrayLike(stream.mapToObj(this::examine));
  }

  @Override
  protected @NotNull Stream<String> stream(final @NotNull LongStream stream) {
    return this.arrayLike(stream.mapToObj(this::examine));
  }

  @Override
  public @NotNull Stream<String> examine(final @Nullable String value) {
    return Stream.of(this.examiner.examine(value));
  }

  private Stream<String> arrayLike(final Stream<Stream<String>> streams) {
    final Stream<String> flattened = flatten(",", streams);
    final Stream<String> indented = indent(flattened);
    return enclose(indented, "[", "]");
  }

  private static Stream<String> enclose(final Stream<String> lines, final String open, final String close) {
    return enclose(lines.collect(Collectors.toList()), open, close);
  }

  private static Stream<String> enclose(final List<String> lines, final String open, final String close) {
    if(lines.isEmpty()) {
      return Stream.of(open + close);
    }
    return Stream.of(
      Stream.of(open),
      indent(lines.stream()),
      Stream.of(close)
    ).reduce(Stream.empty(), Stream::concat);
  }

  private static Stream<String> flatten(final String delimiter, final Stream<Stream<String>> bumpy) {
    final List<String> flat = new ArrayList<>();
    bumpy.forEachOrdered(lines -> {
      if(!flat.isEmpty()) {
        final int last = flat.size() - 1;
        flat.set(last, flat.get(last) + delimiter);
      }
      lines.forEachOrdered(flat::add);
    });
    return flat.stream();
  }

  private static Stream<String> association(final Stream<String> left, final String middle, final Stream<String> right) {
    return association(
      left.collect(Collectors.toList()),
      middle,
      right.collect(Collectors.toList())
    );
  }

  private static Stream<String> association(final List<String> left, final String middle, final List<String> right) {
    final int lefts = left.size();
    final int rights = right.size();

    final int height = Math.max(lefts, rights);
    final int leftWidth = Strings.maxLength(left.stream());

    final String leftPad = lefts < 2 ? "" : Strings.repeat(" ", leftWidth);
    final String middlePad = lefts < 2 ? "" : Strings.repeat(" ", middle.length());

    final List<String> result = new ArrayList<>(height);
    for(int i = 0; i < height; i++) {
      final String l = i < lefts ? Strings.padEnd(left.get(i), leftWidth, ' ') : leftPad;
      final String m = i == 0 ? middle : middlePad;
      final String r = i < rights ? right.get(i) : "";
      result.add(l + m + r);
    }
    return result.stream();
  }

  private static Stream<String> indent(final Stream<String> lines) {
    return lines.map(line -> INDENT_2 + line);
  }

  private static final class Instances {
    static final MultiLineStringExaminer SIMPLE_ESCAPING = new MultiLineStringExaminer(StringExaminer.simpleEscaping());
  }
}
