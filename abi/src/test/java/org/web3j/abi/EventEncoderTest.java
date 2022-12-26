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
package org.web3j.abi;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.web3j.abi.Utils.convert;

public class EventEncoderTest {

    @Test
    public void testBuildEventSignature() {
        final Event ORDERCLAIMED_EVENT = new Event("OrderClaimed",
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Order>() {}));
        ;

//        assertEquals(
//                EventEncoder.buildEventSignature("OrderClaimed(uint256,address,(uint8,uint8,address,address,address,address[],uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256[]))"),
//                ("0x8fca2b3cd2c549ab457e1551e0037543d061007f0720d5a516f197d3ea6983b0"));

        assertEquals(
                EventEncoder.encode(ORDERCLAIMED_EVENT),
                "0x8fca2b3cd2c549ab457e1551e0037543d061007f0720d5a516f197d3ea6983b0"
        );
    }

    public static class Order extends DynamicStruct {
        public BigInteger round;

        public BigInteger orderStatus;

        public String seller;

        public String collection;

        public String payment;

        public List<String> buyer;

        public BigInteger orderID;

        public BigInteger orderAmount;

        public BigInteger startTime;

        public BigInteger tid;

        public BigInteger price;

        public BigInteger limit;

        public BigInteger totalSoldAmount;

        public List<BigInteger> soldAmount;

        public Order(BigInteger round, BigInteger orderStatus, String seller, String collection, String payment, List<String> buyer, BigInteger orderID, BigInteger orderAmount, BigInteger startTime, BigInteger tid, BigInteger price, BigInteger limit, BigInteger totalSoldAmount, List<BigInteger> soldAmount) {
            super(new Uint8(round),
                    new Uint8(orderStatus),
                    new Address(160, seller),
                    new Address(160, collection),
                    new Address(160, payment),
                    new DynamicArray<Address>(
                            Address.class,
                            org.web3j.abi.Utils.typeMap(buyer, Address.class)),
                    new Uint256(orderID),
                    new Uint256(orderAmount),
                    new Uint256(startTime),
                    new Uint256(tid),
                    new Uint256(price),
                    new Uint256(limit),
                    new Uint256(totalSoldAmount),
                    new DynamicArray<Uint256>(
                            Uint256.class,
                            org.web3j.abi.Utils.typeMap(soldAmount, Uint256.class)));
            this.round = round;
            this.orderStatus = orderStatus;
            this.seller = seller;
            this.collection = collection;
            this.payment = payment;
            this.buyer = buyer;
            this.orderID = orderID;
            this.orderAmount = orderAmount;
            this.startTime = startTime;
            this.tid = tid;
            this.price = price;
            this.limit = limit;
            this.totalSoldAmount = totalSoldAmount;
            this.soldAmount = soldAmount;
        }

        public Order(Uint8 round, Uint8 orderStatus, Address seller, Address collection, Address payment, DynamicArray<Address> buyer, Uint256 orderID, Uint256 orderAmount, Uint256 startTime, Uint256 tid, Uint256 price, Uint256 limit, Uint256 totalSoldAmount, DynamicArray<Uint256> soldAmount) {
            super(round, orderStatus, seller, collection, payment, buyer, orderID, orderAmount, startTime, tid, price, limit, totalSoldAmount, soldAmount);
            this.round = round.getValue();
            this.orderStatus = orderStatus.getValue();
            this.seller = seller.getValue();
            this.collection = collection.getValue();
            this.payment = payment.getValue();
            this.buyer = buyer.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.orderID = orderID.getValue();
            this.orderAmount = orderAmount.getValue();
            this.startTime = startTime.getValue();
            this.tid = tid.getValue();
            this.price = price.getValue();
            this.limit = limit.getValue();
            this.totalSoldAmount = totalSoldAmount.getValue();
            this.soldAmount = soldAmount.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
        }
    }


    @Test
    public void testEncode() {
        Event event =
                new Event(
                        "Notify",
                        Arrays.<TypeReference<?>>asList(
                                new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));

        assertEquals(
                EventEncoder.encode(event),
                "0x71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed");
    }

    @Test
    public void testBuildMethodSignature() {
        List<TypeReference<?>> parameters =
                Arrays.<TypeReference<?>>asList(
                        new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {});

        assertEquals(
                EventEncoder.buildMethodSignature("Notify", convert(parameters)),
                "Notify(uint256,uint256)");
    }

    @Test
    void testBuildMethodSignatureWithDynamicStructs() {
        assertEquals(
                "nazzEvent((((string,string)[])[],uint256),(string,string))",
                EventEncoder.buildMethodSignature(
                        AbiV2TestFixture.nazzEvent.getName(),
                        AbiV2TestFixture.nazzEvent.getParameters()));
    }

    @Test
    void testBuildMethodSignatureWithDynamicArrays() {
        assertEquals(
                "nazzEvent2((((string,string)[])[],uint256)[])",
                EventEncoder.buildMethodSignature(
                        AbiV2TestFixture.nazzEvent2.getName(),
                        AbiV2TestFixture.nazzEvent2.getParameters()));
    }
}
