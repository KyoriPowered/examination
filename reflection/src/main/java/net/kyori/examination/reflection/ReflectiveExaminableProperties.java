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

import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.ExaminablePropertySource;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * An examinable property source which provides {@link ExaminableProperty properties} by reflectively examining an object.
 *
 * @since 1.1.0
 */
public interface ReflectiveExaminableProperties extends ExaminablePropertySource {
  /**
   * Creates an examinable property source from the fields in {@code object} annotated with {@link Examine}.
   *
   * @param object the object to be examined
   * @return an examinable property source
   * @since 1.1.0
   */
  static @NonNull ReflectiveExaminableProperties forFields(final @NonNull Object object) {
    return ReflectiveExaminablePropertiesImpl.forFields(object);
  }
}
