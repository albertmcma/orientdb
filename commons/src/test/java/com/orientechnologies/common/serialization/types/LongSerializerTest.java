/*
 * Copyright 2010-2012 Luca Garulli (l.garulli--at--orientechnologies.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orientechnologies.common.serialization.types;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.orientechnologies.common.directmemory.ODirectMemoryPointer;

/**
 * @author ibershadskiy <a href="mailto:ibersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 18.01.12
 */
@Test
public class LongSerializerTest {
  private static final int  FIELD_SIZE = 8;
  private static final Long OBJECT     = 999999999999999999L;
  private OLongSerializer   longSerializer;
  byte[]                    stream     = new byte[FIELD_SIZE];

  @BeforeClass
  public void beforeClass() {
    longSerializer = new OLongSerializer();
  }

  public void testFieldSize() {
    Assert.assertEquals(longSerializer.getObjectSize(null), FIELD_SIZE);
  }

  public void testSerialize() {
    longSerializer.serialize(OBJECT, stream, 0);
    Assert.assertEquals(longSerializer.deserialize(stream, 0), OBJECT);
  }

  public void testSerializeNative() {
    longSerializer.serializeNative(OBJECT, stream, 0);
    Assert.assertEquals(longSerializer.deserializeNativeObject(stream, 0), OBJECT);
  }

  public void testNativeDirectMemoryCompatibility() {
    longSerializer.serializeNative(OBJECT, stream, 0);

    ODirectMemoryPointer pointer = new ODirectMemoryPointer(stream);
    try {
      Assert.assertEquals(longSerializer.deserializeFromDirectMemoryObject(pointer, 0), OBJECT);
    } finally {
      pointer.free();
    }

  }
}
