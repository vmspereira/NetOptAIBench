/**
* Copyright 2012-2017,
* Centro Algoritmi
* University of Minho
*
* This is free software: you can redistribute it and/or modify
* it under the terms of the GNU Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Public License for more details.
*
* You should have received a copy of the GNU Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
* 
* @author Vítor Pereira
*/
package pt.uminho.netopt.aibench.gui.options;


import com.l2fprod.common.beans.BaseBeanInfo;


public class EAGeneralBeanBeanInfo extends BaseBeanInfo
{

    public EAGeneralBeanBeanInfo()
    {
        super(EAGeneralBean.class);
        addProperty("minWeight").setCategory("Link State Routing");
        addProperty("maxWeight").setCategory("Link State Routing");
        addProperty("DefaultBandWidth").setCategory("Topology");
        addProperty("DefaultDelay").setCategory("Topology");
        
        addProperty("DeftThreshold").setCategory("DEFT/PEFT/SR");
        addProperty("DeftApplyThreshold").setCategory("DEFT/PEFT/SR");
        
        addProperty("HybridSRPenaltyFactor").setCategory("Segment Routing");
        
        addProperty("PDefault").setCategory("Node-p Values");
        addProperty("PMin").setCategory("Node-p Values");
        addProperty("PMax").setCategory("Node-p Values");
        addProperty("PIntScaller").setCategory("Node-p Values");

        
        addProperty("TwoPointCrossover").setCategory("EA");
        addProperty("UniformCrossover").setCategory("EA");
        addProperty("RandomMutation").setCategory("EA");
        addProperty("IncrementalMutation").setCategory("EA");
        
        addProperty("NumberParallelThreads").setCategory("Threading");
        
        addProperty("LFRecoveryProperty").setCategory("SR Simulator");
        addProperty("SRNodepIterations").setCategory("SR Simulator");
    }
}
