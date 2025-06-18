/*
 * Copyright 2010-2011 Kevin Seim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.beanio;

/**
 * Provides support for <code>BeanReaderErrorHandler</code> implementations.  The
 * <code>handleError</code> method delegates to other methods that can be overridden
 * to handle specific error types.  If a method is not overridden for a specific
 * error type, the method will simply rethrow the exception by default.
 * 
 * @author Kevin Seim
 * @since 1.0
 */
public class BeanReaderErrorHandlerSupport implements BeanReaderErrorHandler {

    /**
     * Delegates error handling based on the exception type.
     * @param ex the <code>BeanReaderException</code> to handle
     * @throws Exception if the BeanReaderException is rethrown or this error
     *   handler throws a new Exception
     */
    @Override
    public final void handleError(BeanReaderException ex) throws Exception {
        if (ex instanceof InvalidRecordException) {
            invalidRecord((InvalidRecordException)ex);
        }
        else if (ex instanceof UnexpectedRecordException) {
            unexpectedRecord((UnexpectedRecordException)ex);
        }
        else if (ex instanceof MalformedRecordException) {
            malformedRecord((MalformedRecordException)ex);
        }
        else if (ex instanceof UnidentifiedRecordException) {
            unidentifiedRecord((UnidentifiedRecordException)ex);
        }
        else {
            fatalError(ex);
        }
    }

    /**
     * Handles <code>InvalidRecordException</code> and <code>InvalidRecordGroupException</code> errors.
     * By default, this method simply rethrows the exception.
     * @param ex the <code>InvalidRecordException</code> to handle
     * @throws Exception if the exception is not handled
     */
    public void invalidRecord(InvalidRecordException ex) throws Exception {
        throw ex;
    }

    /**
     * Handles <code>UnexpectedRecordException</code> errors.  By default, this method
     * simply rethrows the exception.
     * @param ex the <code>UnexpectedRecordException</code> to handle
     * @throws Exception if the exception is not handled
     */
    public void unexpectedRecord(UnexpectedRecordException ex) throws Exception {
        throw ex;
    }
    
    /**
     * Handles <code>UnidentifiedRecordException</code> errors.  By default, this method
     * simply rethrows the exception.
     * @param ex the <code>UnidentifiedRecordException</code> to handle
     * @throws Exception if the exception is not handled
     */
    public void unidentifiedRecord(UnidentifiedRecordException ex) throws Exception {
        throw ex;
    }
    
    /**
     * Handles <code>MalformedRecordException</code> errors.  By default, this method
     * simply rethrows the exception.
     * @param ex the <code>MalformedRecordException</code> to handle
     * @throws Exception if the exception is not handled
     */
    public void malformedRecord(MalformedRecordException ex) throws Exception {
        throw ex;
    }
    
    /**
     * Handles errors not handled by any other method.  By default, this method
     * simply rethrows the exception.
     * @param ex the <code>BeanReaderException</code> to handle
     * @throws Exception if the exception is not handled
     */
    public void fatalError(BeanReaderException ex) throws Exception {
        throw ex;
    }
}
