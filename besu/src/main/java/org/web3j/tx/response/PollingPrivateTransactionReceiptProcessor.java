/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.tx.response;

import java.io.IOException;
import java8.util.Optional;

import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.besu.response.privacy.PrivateTransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

public class PollingPrivateTransactionReceiptProcessor extends PrivateTransactionReceiptProcessor {
    private final long sleepDuration;
    private final int attempts;

    public PollingPrivateTransactionReceiptProcessor(Besu besu, long sleepDuration, int attempts) {
        super(besu);
        this.sleepDuration = sleepDuration;
        this.attempts = attempts;
    }

    @Override
    public PrivateTransactionReceipt waitForTransactionReceipt(String transactionHash)
            throws IOException, TransactionException {

        return getTransactionReceipt(transactionHash, sleepDuration, attempts);
    }

    private PrivateTransactionReceipt getTransactionReceipt(
            String transactionHash, long sleepDuration, int attempts)
            throws IOException, TransactionException {

        for (int i = 0; i < attempts; i++) {
            Optional<PrivateTransactionReceipt> receiptOptional =
                    sendTransactionReceiptRequest(transactionHash);

            if (receiptOptional.isPresent()) {
                return receiptOptional.get();
            }

            // Sleep unless it is the last attempt.
            if (i < attempts - 1) {
                try {
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    throw new TransactionException(e);
                }
            }
        }

        throw new TransactionException(
                "Transaction receipt was not generated after "
                        + ((sleepDuration * attempts) / 1000
                                + " seconds for transaction: "
                                + transactionHash),
                transactionHash);
    }
}
