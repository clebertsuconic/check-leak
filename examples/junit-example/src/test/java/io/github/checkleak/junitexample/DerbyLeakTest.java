/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.checkleak.junitexample;

import java.sql.Connection;
import java.sql.DriverManager;

import io.github.checkleak.core.CheckLeak;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DerbyLeakTest {


   @Test
   public void testDerby() throws Exception {
      for (int i = 0; i < 10; i++) {
         Connection connection = DriverManager.getConnection("jdbc:derby:memory:test" + i + ";create=true");
         try {
            connection.createStatement().execute("create table test (a varchar(255))");
         } catch (Exception e) {
            e.printStackTrace();
            connection.createStatement().execute("drop table test");
         }

         connection.close();
         connection = null;

         try {
            DriverManager.getConnection("jdbc:derby:memory:test" + i + ";drop=true");
         } catch (Exception ignored) {
            // it always throws an exception on shutdown
         }
      }

      try {
         DriverManager.getConnection("jdbc:derby:;shutdown=true;deregister=false");
      } catch (Exception e) {
      }
      CheckLeak checkLeak = new CheckLeak();
      for (int i = 0; i < 100; i++) {
         checkLeak.forceGC();
      }
      if (checkLeak.getAllObjects("org.apache.derby.impl.io.vfmem.DataStore").length != 0) {
         Object[] objects = checkLeak.getAllObjects("org.apache.derby.impl.io.vfmem.DataStore");

         System.out.println(checkLeak.exploreObjectReferences("org.apache.derby.impl.io.vfmem.DataStore", 10, 10, true));
         Assertions.fail();
      }


   }

}
