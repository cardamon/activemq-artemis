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

package org.apache.activemq.artemis.utils.actors;

/**
 * This abstract class will encapsulate
 * ThreadLocals to determine when a class is a handler.
 * This is because some functionality has to be avoided if inHandler().
 *
 */
public abstract class HandlerBase {

   private static class Counter {
      int count = 0;
   }

   /** an actor could be used within an OrderedExecutor. So we need this counter to decide if there's a Handler anywhere in the stack trace */
   private static final ThreadLocal<Counter> inHandler = ThreadLocal.withInitial(() -> new Counter());

   protected static void enter() {
      inHandler.get().count++;
   }

   public static boolean inHandler() {
      int count = inHandler.get().count;
      if (count == 0) {
         inHandler.remove();
      }
      return count > 0;
   }

   protected static void leave() {
      int count = --inHandler.get().count;
      if (count == 0) {
         inHandler.remove();
      }
   }

}
