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
import org.web3j.abi.datatypes.reflection.Parameterized;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.web3j.abi.Utils.convert;

public class EventEncoderTest {

    @Test
    public void testBuildEventSignature() {
//        assertEquals(
//                EventEncoder.buildEventSignature("OrderClaimed(uint256,address,(uint8,uint8,address,address,address,address[],uint256,uint256,uint256,uint256,uint256,uint256,uint256,uint256[]))"),
//                ("0x8fca2b3cd2c549ab457e1551e0037543d061007f0720d5a516f197d3ea6983b0"));
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
