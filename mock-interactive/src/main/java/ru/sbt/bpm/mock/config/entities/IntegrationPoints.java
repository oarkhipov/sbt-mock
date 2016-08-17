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

@XStreamAlias("integrationPoints")
@Data
public class IntegrationPoints {

    @XStreamImplicit(itemFieldName = "integrationPoint")
    private Set<IntegrationPoint> integrationPoints;

    public IntegrationPoints() {
        integrationPoints = initSet();
    }

    public static Set<IntegrationPoint> initSet() {
        return new TreeSet<IntegrationPoint>(new IntegrationPointComparator());
    }

    public IntegrationPoint getIntegrationPointByName(String name) {
        for (IntegrationPoint integrationPoint : getIntegrationPoints()) {
            if (integrationPoint.getName().equals(name)) {
                return integrationPoint;
            }
        }

        throw new NoSuchElementException("No Integration point with name [" + name + "]");
    }

    public Set<IntegrationPoint> getIntegrationPoints() {
        if (integrationPoints == null) {
            integrationPoints = initSet();
        }
        return integrationPoints;
    }

    private static class IntegrationPointComparator implements Comparator<IntegrationPoint> {
        @Override
        public int compare(IntegrationPoint o1, IntegrationPoint o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public void fixTypes() {
        if (integrationPoints != null) {
            Set<IntegrationPoint> integrationPointSet = initSet();
            integrationPointSet.addAll(integrationPoints);
            this.integrationPoints = integrationPointSet;
        }
    }
}
