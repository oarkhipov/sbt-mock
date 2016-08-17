/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by sbt-hodakovskiy-da on 30.01.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */
@XStreamAlias("systems")
@Data
public class Systems {

    @XStreamImplicit(itemFieldName = "system")
    private Set<System> systems;

    public Systems() {
        systems = initSet();
    }

    public static Set<System> initSet() {
        return new TreeSet<System>(new SystemComparator());
    }

    public System getSystemByName(String name) {
        for (System system : systems) {
            if (system.getSystemName().equals(name)) {
                return system;
            }
        }

        throw new NoSuchElementException("No system with name [" + name + "]");

    }

    public Set<System> getSystems() {
        if (systems == null) {
            systems = initSet();
        }
        return systems;
    }

    private static class SystemComparator implements Comparator<System> {
        @Override
        public int compare(System o1, System o2) {
            return (o1.getProtocol().name() + o1.getSystemName())
                    .compareTo(o2.getProtocol().name() + o2.getSystemName());
        }
    }

    public void fixTypes() {
        if (systems != null) {
            Set<System> systemSet = initSet();
            systemSet.addAll(systems);
            this.systems = systemSet;
        }
    }
}
