/**
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.ipaas.core;

public class IPaasServerException extends RuntimeException {

	private static final long serialVersionUID = 3476018743129184217L;

	public IPaasServerException() {
		super();
	}

	public IPaasServerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IPaasServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public IPaasServerException(String message) {
		super(message);
	}

	public IPaasServerException(Throwable cause) {
		super(cause);
	}

	public static RuntimeException launderThrowable(Throwable cause) {
	    return launderThrowable("An error has occurred.", cause);
	  }

	  public static RuntimeException launderThrowable(String message, Throwable cause) {
	    if (cause instanceof RuntimeException) {
	      return ((RuntimeException) cause);
	    } else if (cause instanceof Error) {
	      throw ((Error) cause);
	    } else {
	      throw new IPaasServerException(message, cause);
	    }
	  }

}
