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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

final class ReflectiveExaminablePropertiesImpl implements ReflectiveExaminableProperties {
  private final List<Supplier<ExaminableProperty>> properties;

  private ReflectiveExaminablePropertiesImpl(final List<Supplier<ExaminableProperty>> properties) {
    this.properties = properties;
  }

  public static ReflectiveExaminablePropertiesImpl forFields(final Object object) {
    final List<Supplier<ExaminableProperty>> properties = new ArrayList<>();
    for(final Field field : object.getClass().getDeclaredFields()) {
      final Examine examine = field.getAnnotation(Examine.class);
      if(examine != null) {
        field.setAccessible(true); // TODO(kashike): does this break on new Java versions?

        final String name = name(field, examine);
        final MethodHandle handle;
        try {
          handle = MethodHandles.lookup().unreflectGetter(field);
        } catch(final IllegalAccessException e) {
          // TODO(kashike): how to handle errors?
          e.printStackTrace();
          continue;
        }
        properties.add(() -> {
          final Object value;
          try {
            value = handle.invoke(object);
          } catch(final Throwable e) {
            // TODO(kashike): how to handle errors?
            e.printStackTrace();
            return null;
          }
          return ExaminableProperty.of(name, value);
        });
      }
    }
    return new ReflectiveExaminablePropertiesImpl(properties);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return this.properties.stream()
      .map(Supplier::get)
      .filter(Objects::nonNull);
  }

  private static String name(final Field field, final Examine examine) {
    String name = examine.name();
    if(name.isEmpty()) {
      name = field.getName();
    }
    return name;
  }
}
