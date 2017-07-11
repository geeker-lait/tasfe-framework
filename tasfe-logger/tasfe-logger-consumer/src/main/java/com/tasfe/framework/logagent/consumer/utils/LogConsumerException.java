/*
 *    COPYRIGHT 2016-~ THE WWW.MOBANK.COM ARCH TEAM
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.tasfe.framework.logagent.consumer.utils;

/**
 * @author adam
 * @version 1.0
 * @since JDK 1.7
 */
public class LogConsumerException extends Exception {
    public LogConsumerException() {
        super();
    }

    public LogConsumerException(String message) {
        super(message);
    }

    public LogConsumerException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogConsumerException(Throwable cause) {
        super(cause);
    }
}
