/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * $Id: IndexFileTest.java 1831 2013-05-16 01:39:51Z shijia.wxr $
 */
package com.alibaba.rocketmq.store.index;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class IndexFileTest {
    private static final int HASH_SLOT_NUM = 100;
    private static final int INDEX_NUM = 400;

    @Test
    public void test_put_index() throws Exception {
        IndexFile indexFile = new IndexFile("100", HASH_SLOT_NUM, INDEX_NUM, 0, 0);
        for (long i = 0; i < (INDEX_NUM - 1); i++) {
            boolean putResult = indexFile.putKey(Long.toString(i), i, System.currentTimeMillis());
            assertTrue(putResult);
        }

        // put over index file capacity.
        boolean putResult = indexFile.putKey(Long.toString(400), 400, System.currentTimeMillis());
        assertFalse(putResult);
    
        indexFile.destroy(0);
    }


    @Test
    public void test_put_get_index() throws Exception {
        IndexFile indexFile = new IndexFile("200", HASH_SLOT_NUM, INDEX_NUM, 0, 0);
    
        for (long i = 0; i < (INDEX_NUM - 1); i++) {
            boolean putResult = indexFile.putKey(Long.toString(i), i, System.currentTimeMillis());
            assertTrue(putResult);
        }

        // put over index file capacity.
        boolean putResult = indexFile.putKey(Long.toString(400), 400, System.currentTimeMillis());
        assertFalse(putResult);
    
        final List<Long> phyOffsets = new ArrayList<Long>();
        indexFile.selectPhyOffset(phyOffsets, "60", 10, 0, Long.MAX_VALUE, true);
        assertFalse(phyOffsets.isEmpty());
        assertEquals(1, phyOffsets.size());

        indexFile.destroy(0);
    }
}
